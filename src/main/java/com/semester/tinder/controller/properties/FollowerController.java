package com.semester.tinder.controller.properties;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.semester.tinder.dto.request.Follower.DetailFollowerDTO;
import com.semester.tinder.dto.request.Follower.DetailUserMatchingDTO;
import com.semester.tinder.dto.request.Follower.LikeUserRequest;
import com.semester.tinder.dto.request.Follower.StatusFollower;
import com.semester.tinder.dto.request.RabbitMQ.Notification;
import com.semester.tinder.dto.request.Test.Message;
import com.semester.tinder.dto.response.ApiResponse;
import com.semester.tinder.entity.*;
import com.semester.tinder.repository.IFollowerRepo;
import com.semester.tinder.repository.IProfileRepo;
import com.semester.tinder.repository.IUserRepo;
import com.semester.tinder.repository.IVipUsersRepo;
import com.semester.tinder.service.RappidMQ.RabbitMQService;
import com.semester.tinder.service.message.MessageFirebaseService;
import com.semester.tinder.service.redis.HandleCountLikes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/public/follower")
public class FollowerController {


    @Autowired
    private IUserRepo _iUserRepo;

    @Autowired
    private IFollowerRepo _iFollowerRepo;

    @Autowired
    private IProfileRepo _iProfileRepo;

    @Autowired
    private RabbitMQService rabbitMQService;

    @Autowired
    private MessageFirebaseService _messageFireBaseService;

    @Autowired
    private IVipUsersRepo _iVipUsersRepo;

    @Autowired
    private HandleCountLikes _handleCountLikes;

    // xử lý user like someone ( someone had liked user )
    @PostMapping("/like")
    public ResponseEntity<ApiResponse<String>> interact(@RequestBody LikeUserRequest likeUserRequest) throws ExecutionException, InterruptedException {

        ApiResponse<String> R = new ApiResponse<>();
        R.setMessage("success");
        R.setCode(200);
        // save message in fire base:
        CompletableFuture.runAsync( ()  -> {
            Notification no = new Notification();
            try {
                // guu thong bao cho node js:
                Optional<User> user1 = _iUserRepo.findById(likeUserRequest.getU_id1() );

                // lấy ra id user 2 -> đây là thằng bị đong like:
                Optional<User> user2 = _iUserRepo.findById( likeUserRequest.getU_id2() );

                switch ( likeUserRequest.getStatus() ){
                    case "Like":
                        // nếu mà người kia đã like minh thi
                        // CHECK XEM LÀ MINH ĐÃ ĐƯỢC NÓ LIKE CHƯA:
                        Optional<Follower> new_F = _iFollowerRepo.findIWasLikedYet( likeUserRequest.getU_id2(), likeUserRequest.getU_id1() );

                        if(new_F.isPresent() ){

                            if( StatusFollower.LIKE.getName().equals( new_F.get().getStatus() )  ){

                                new_F.get().setStatus(StatusFollower.MATCHING.getName());
                                new_F.get().setMatch_at_time( new Date());
                                _iFollowerRepo.save(new_F.get());

                                //  rabbitMQService.sendMessage("node_channel", message);
                                //   Result.setResult("CONGRATS! MATCHING " + user1.get().getFullname() + " AND "+ user2.get().getFullname() + "  SUCCESSFUL");

                                // 1. save notify in firebase
                                no.setId( UUID.randomUUID().toString() );
                                no.setCodeNotify(1);
                                no.setTitle("thông báo ghép đôi thành công!");
                                no.setRead(false);
                                no.setContent("ghép đôi giữa " + user1.orElseThrow().getFullname() + " và " + user2.orElseThrow().getFullname() + " thành công");
                                no.setReceiverId( likeUserRequest.getU_id1() );
                                no.setIdMatching2( new_F.get().getId() );
                                no.setImage(   user2.get().getImages() == null || user2.get().getImages().isEmpty() ? "" : _messageFireBaseService.AnImage(user2.get().getImages())  ); //
                                //    no.setIdMatching2( iLike.getId() );
                                //no.setUrlAttached("localhost:3000/123/123");
                                no.setTimeNotify( new Date());

                                rabbitMQService.sendMessage("node_channel", no);

                                Notification no2 = new Notification();

                                no2.setId( UUID.randomUUID().toString() );
                                no2.setCodeNotify(1);
                                no2.setTitle("thông báo ghép đôi thành công!");
                                no2.setRead(false);
                                no2.setContent("ghép đôi giữa " + user1.orElseThrow().getFullname() + " và " + user2.orElseThrow().getFullname() + " thành công");
                                no2.setReceiverId( likeUserRequest.getU_id2() );
                                no2.setIdMatching2( new_F.get().getId() );
                                no2.setImage(  user1.get().getImages() == null || user1.get().getImages().isEmpty() ? "" : _messageFireBaseService.AnImage(user1.get().getImages())  );
                                //    no.setIdMatching2( iLike.getId() );
                                //no.setUrlAttached("localhost:3000/123/123");
                                no2.setTimeNotify( new Date());

                                rabbitMQService.sendMessage("node_channel", no2);
                                _messageFireBaseService.createNotify(no);
                                _messageFireBaseService.createNotify(no2);
                            }

                        }else{
                            // xử lý check gói xem có gói like thoả mái ko? -> chay vao if
                            // neu ko co chay vao else check xem da vuot qua so luong like hang ngay chua?

                            // user co dang ky goi ko? -> check xem thoi gian goi con hieu luc ko?
                            Optional<VipUsers> vip_u = _iVipUsersRepo.findIsExist(likeUserRequest.getU_id1() );

                            if(vip_u.isEmpty() ){
                                // neu ko dang ky goi tinder gi:
                               int result = _handleCountLikes.like( String.valueOf( likeUserRequest.getU_id1() ) );
                                if( result == 400 ){
                                    // error gui thong bao qua so luong like cho nguoi dung:
                                    no.setCodeNotify(2);
                                    no.setTitle("Xin lỗi thao tác không thành công! Bạn đã vượt quá số lượng like trong một ngày!"); // bo sung doan nay vao react guu du lieu len!
                                    no.setRead(false);
                                    no.setContent("Vui lòng nâng cấp gói Tinder để thoả thích like.");
                                    no.setReceiverId( likeUserRequest.getU_id1() );
                                    no.setTimeNotify( new Date());
                                    rabbitMQService.sendMessage("node_channel", no);

                                    break;
                                }
                            }else{
                                // chi can lay thoi gian endtime cua goi day va so sanh voi thoi gian hien tai
                                LocalDate currentDate = LocalDate.now();
                                if(currentDate.isAfter(vip_u.get().getEnd_time().toLocalDate())){ // dung sau la het han
                                    // ta se xoa di cai nay trong bang tinder vip:
                                    _iVipUsersRepo.delete( vip_u.get() );
                                }
                            }

                            // nếu như người kia chưa like mình và mình like họ:
                            Follower iLike = new Follower();

                            iLike.setImplementer( user1.orElseThrow() );
                            iLike.setAffected_person(user2.orElseThrow() );
                            iLike.setStatus( StatusFollower.LIKE.getName() );

                            _iFollowerRepo.save(iLike);


                            //  Result.setResult("YOU LIKED " + user2.get().getFullname()+ " DONE!");

                            // 1. save notify in firebase

                            // 2. send message for rabbitMQ

                            no.setId( UUID.randomUUID().toString() );
                            no.setSenderId(likeUserRequest.getU_id1() );
                            no.setCodeNotify(2);
                            no.setTitle("Thông báo"); // bo sung doan nay vao react guu du lieu len!
                            no.setRead(false);
                            no.setContent(user1.orElseThrow().getFullname() + " đã thích bạn");
                            no.setReceiverId( likeUserRequest.getU_id2() );
                            no.setImage( user1.get().getImages() == null || user1.get().getImages().isEmpty() ? "" : _messageFireBaseService.AnImage(user1.get().getImages()) );
                            // no.setUrlAttached("localhost:3000/123/123");
                            no.setTimeNotify( new Date());
                            rabbitMQService.sendMessage("node_channel", no);
                            _messageFireBaseService.createNotify(no);
                            // 1. guu thong bao cho node js:

                        }
                        break;
                    case "Block":

                        Optional<Follower> f1 = _iFollowerRepo.findUserIsExist(likeUserRequest.getU_id1(), likeUserRequest.getU_id2());


                        if(f1.isEmpty()){
                            Follower iLike1 = new Follower();

                            iLike1.setImplementer( user1.orElseThrow() );
                            iLike1.setAffected_person(user2.orElseThrow() );
                            iLike1.setStatus( StatusFollower.BLOCK.getName() );

                            _iFollowerRepo.save(iLike1);
                        }else{
                            f1.get().setStatus(StatusFollower.BLOCK.getName() );
                        }
                        // 1. save notify in firebase
                        no.setId( UUID.randomUUID().toString() );
                        no.setCodeNotify(2);
                        no.setTitle("Thông báo"); // bo sung doan nay vao react guu du lieu len!
                        no.setRead(false);
                        no.setContent("Bạn đã block "+ user2.orElseThrow().getFullname() +" thành công");
                        no.setReceiverId( likeUserRequest.getU_id1() );
                        // no.setUrlAttached("localhost:3000/123/123");
                        no.setTimeNotify( new Date());
                        _messageFireBaseService.createNotify(no);
                        // 1. guu thong bao cho node js:
                        rabbitMQService.sendMessage("node_channel", no);
                        break;
                    case "Canceled":
                        Follower iLike2 = new Follower();

                        iLike2.setImplementer( user1.orElseThrow() );
                        iLike2.setAffected_person(user2.orElseThrow() );
                        iLike2.setStatus( StatusFollower.CANCELED.getName() );

                        _iFollowerRepo.save(iLike2);

                        // 1. save notify in firebase

                        // 2. send message for rabbitMQ

                        //     Result.setResult("YOU CANCELED " + user2.get().getFullname()+ " DONE!");
                        break;
                    default:
                        break;
                }
            }catch (Exception e){
                no.setCodeNotify(2);
                no.setTitle("Có lỗi xảy ra:");
                no.setContent("Thao tác xảy ra bị lỗi! ");
                no.setReceiverId(likeUserRequest.getU_id1());
                no.setTimeNotify( new Date());

                rabbitMQService.sendMessage("node_channel", no);
            }

        } );


        R.setResult( "operation " + likeUserRequest.getStatus() + " User id: " +likeUserRequest.getU_id2()  );
        return ResponseEntity.ok(R);
    }




    /*
    * CÁI MOI DUOC HOC TU Optional:
    * new_F.ifPresent(f -> {
// Đoạn code này chỉ chạy nếu new_F không phải là Optional rỗng
if ("Like".equals(f.getStatus())) {
System.out.println("chạy được vào vòng if");
f.setStatus(StatusFollower.MATCHING.getName());
f.setMatch_at_time(new Date());
_iFollowerRepo.save(f);
Result.setResult("CONGRATS! MATCHING " + user1.get().getFullname() + " AND " + user2.get().getFullname() + " SUCCESSFUL");
}
});
    *
    * */

    // lay ve list tin nhan
    @GetMapping("/list/matching")
    public ResponseEntity<ApiResponse<List<UsersFollower>>> ListMatching(@RequestParam int id ){

        // lấy ra các id trong u_id1 và u_id2 của follower:

        List<Integer> list_users = _iFollowerRepo.findUsers1(id);

        List<Integer> list_users2 = _iFollowerRepo.findUsers2(id);

        list_users.addAll(list_users2);

        List<Integer> newListUsers = list_users.stream().toList();

        List<UsersFollower> arrUF = new ArrayList<>();

        for( int i : newListUsers ){

            UsersFollower newF = new UsersFollower();
            Optional<User> u = _iUserRepo.findById(i);

            Optional<Follower> f = _iFollowerRepo.findUserFollow( id, i );

            newF.setId(i);
            newF.setId_matching(f.orElseThrow().getId());
            newF.setFullname( u.orElseThrow().getFullname() );
            newF.setImages( u.get().getImages() == null || u.get().getImages().isEmpty() ? "" : _messageFireBaseService.AnImage(u.get().getImages()) );

            arrUF.add( newF );
        }

        ApiResponse<List<UsersFollower>> result = new ApiResponse<>();
        result.setMessage("successfully");
        result.setCode(200);
        result.setResult( arrUF );

        return ResponseEntity.ok(result);
    }

    @GetMapping("/detail/matching")
    public ResponseEntity<ApiResponse<DetailUserMatchingDTO>> UserDetail( @RequestBody DetailFollowerDTO likeUserRequest ) throws ExecutionException, InterruptedException {

        // lấy ra profile của user + fullname + images
        Optional<User> u = _iUserRepo.findById(likeUserRequest.getU_id2());
        Optional<Follower> f = _iFollowerRepo.findUserFollow( likeUserRequest.getU_id1(), likeUserRequest.getU_id2() );
        Optional<Profile> p = _iProfileRepo.findByUser(u.orElseThrow());

        DetailUserMatchingDTO dtuDTO = new DetailUserMatchingDTO();
        dtuDTO.setFullname( u.get().getFullname() );
        dtuDTO.setBio( p.orElseThrow().getBio() );
        dtuDTO.setRelationship_goals( p.get().getRelationship_goals() );
        dtuDTO.setInterests( p.get().getInterests() );
        dtuDTO.setHeight( p.get().getHeight() );
        dtuDTO.setLanguages( p.get().getLanguages() );
        dtuDTO.setAge( p.get().getAge() );
        dtuDTO.setDate_birth( p.get().getDate_birth() );
        dtuDTO.setPassions( p.get().getPassions() );
        dtuDTO.setAbout_me( p.get().getAbout_me() );
        dtuDTO.setLife_style( p.get().getLife_style() );
        dtuDTO.setBasic( p.get().getBasic() );
        dtuDTO.setDate_matching( f.orElseThrow().getMatch_at_time() );
        dtuDTO.setImages( u.orElseThrow().getImages() );

        // lấy ra tin 30 tin nhắn sớm nhất trong firebase
        Firestore dbFirestore = FirestoreClient.getFirestore();

        CollectionReference collectionReference = dbFirestore.collection("TestMessage"); // thay thế lại bằng collection của mình

        ApiFuture<QuerySnapshot> future =collectionReference.orderBy("timeSent", Query.Direction.DESCENDING).whereEqualTo("matchId", f.get().getId()).limit(30).offset(likeUserRequest.getOffSet()).get(); //.offset(offSet)

        QuerySnapshot querySnapshot = future.get();

        List<Message> messages = new ArrayList<>();

        if( !querySnapshot.isEmpty() ){
            for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()){
                messages.add(documentSnapshot.toObject( Message.class ));
            }
        }

        dtuDTO.setMessages( messages );

        ApiResponse<DetailUserMatchingDTO> result = new ApiResponse<>();
        result.setMessage("successfully");
        result.setCode(200);
        result.setResult( dtuDTO );


        return ResponseEntity.ok( result );
    }


    @GetMapping("/test/firebase")
    public ResponseEntity<List<Message>> test( ) throws ExecutionException, InterruptedException{

        // lấy ra tin 30 tin nhắn sớm nhất trong firebase
        Firestore dbFirestore = FirestoreClient.getFirestore();

        CollectionReference collectionReference = dbFirestore.collection("TestMessage"); // thay thế lại bằng collection của mình

        ApiFuture<QuerySnapshot> future =collectionReference.orderBy("timeSent", Query.Direction.DESCENDING).whereEqualTo("matchId", 4 ).limit(30).offset(0).get(); //.offset(offSet)

        QuerySnapshot querySnapshot = future.get();

        List<Message> messages = new ArrayList<>();

        if( !querySnapshot.isEmpty() ){
            for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()){
                messages.add(documentSnapshot.toObject( Message.class ));
            }
        }
        return ResponseEntity.ok( messages );

    }


    @PostMapping("/unmatched")
    public ResponseEntity<ApiResponse<String>> unMatch(@RequestBody LikeUserRequest likeUserRequest){

        Optional<Follower> new_F = _iFollowerRepo.findUserFollow( likeUserRequest.getU_id1(), likeUserRequest.getU_id2() );

        ApiResponse<String> resultResponse = new ApiResponse<>();

        if(new_F.isEmpty() ){

            resultResponse.setMessage(" data is not found ");
            resultResponse.setCode(404);
            return ResponseEntity.ok( resultResponse );

        }

        _iFollowerRepo.delete( new_F.get() );

        resultResponse.setMessage(" Successfully manipulation  ");
        resultResponse.setCode(200);
        resultResponse.setResult("UnMatch successfully");

        return ResponseEntity.ok( resultResponse );
    }

    // danh sách những người đã like mình
    @GetMapping("/list/liked")
    public ResponseEntity<ApiResponse<List<UsersFollower>>> listLiked(@RequestParam int id ){

        List<UsersFollower> newArray = new ArrayList<>();

        ApiResponse<List<UsersFollower>> res = new ApiResponse<>();

        List<Integer> list_users2 = _iFollowerRepo.findLiked(id);

        List<Integer> newListUsers = list_users2.stream().toList();

        for( int i : newListUsers ){

            UsersFollower newF = new UsersFollower();
            Optional<User> u = _iUserRepo.findById(i);

            Optional<Follower> f = _iFollowerRepo.findUserFollow( id, i );

            newF.setId(i);
            newF.setFullname( u.orElseThrow().getFullname() );
            newF.setImages( u.get().getImages() == null || u.get().getImages().isEmpty() ? "" : _messageFireBaseService.AnImage(u.get().getImages()) );

            newArray.add( newF );
        }

        res.setMessage("Get Successfully");
        res.setCode(200);
        res.setResult(newArray);
        return ResponseEntity.ok( res );
    }


    // thêm laị lượt like mới cho nguoi dung khi qua mot ngay:
    @GetMapping("/update/new-like")
    public ApiResponse<String> updateLikeNewADay(){
        ApiResponse<String> a = new ApiResponse<>();
        a.setMessage("update success");
        a.setCode(200);
        a.setResult(_handleCountLikes.testOnDemo());
        return a;
    }


}
