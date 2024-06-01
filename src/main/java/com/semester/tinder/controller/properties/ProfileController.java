package com.semester.tinder.controller.properties;


import com.semester.tinder.dto.firebase.DeleteImageDTO;
import com.semester.tinder.dto.request.Profile.ProfileRequest;
import com.semester.tinder.dto.request.Profile.ProfileUpdate;
import com.semester.tinder.dto.request.Profile.UpdateRequestProfile;
import com.semester.tinder.dto.response.ApiResponse;
import com.semester.tinder.dto.response.InformsProfile;
import com.semester.tinder.entity.Profile;
import com.semester.tinder.entity.User;
import com.semester.tinder.repository.IProfileRepo;
import com.semester.tinder.repository.IUserRepo;
import com.semester.tinder.service.message.MessageFirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/public/profile") // can token
public class ProfileController {

    @Autowired
    private IProfileRepo _iprofileRepo;

    @Autowired
    private IUserRepo _iuserRepo;

    @Autowired
    public MessageFirebaseService _messageFireBaseService;



    @GetMapping("/getId")
    public ResponseEntity<ApiResponse<InformsProfile>> getId(@RequestParam int id){

        ApiResponse<InformsProfile> result = new ApiResponse<>();

        Optional<User> u = _iuserRepo.findById(id);

        Optional<Profile> p = _iprofileRepo.findByUser(u.orElse(null));

        InformsProfile inf = new InformsProfile();

        inf.setIdProfile( p.get().getId() );
        inf.setFullname(u.get().getFullname());
        inf.setEmail(u.get().getEmail());
        inf.setImages( u.get().getImages() );
        inf.setPhone_number(u.get().getPhone_number());
        inf.setBio( p.get().getBio() );
        inf.setAge( p.get().getAge() );
        inf.setHeight( p.get().getHeight() );
        inf.setInterests( p.get().getInterests() );
        inf.setLanguages( p.get().getLanguages() );
        inf.setPassions( p.get().getPassions() );
        inf.setAbout_me( p.get().getAbout_me() );
        inf.setDate_birth( p.get().getDate_birth() );
        inf.setRelationship_goals( p.get().getRelationship_goals() );
        inf.setGender( u.get().getGender() );

        if( p.isEmpty() ){
            result.setMessage("error! data not found");
            result.setCode(404);
            return ResponseEntity.ok(result);
        }

        result.setMessage("successfully");
        result.setCode(200);
        result.setResult( inf );
        return ResponseEntity.ok(result);

    }



    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<String>> deleteP(@RequestParam int id){

        ApiResponse<String> result = new ApiResponse<>();

        Optional<User> u = _iuserRepo.findById(id);

        Optional<Profile> p = _iprofileRepo.findByUser( u.orElse(null) );

        if( p.isEmpty() ){
            result.setMessage("error! data not found");
            result.setCode(404);
            return ResponseEntity.ok(result);
        }

        _iprofileRepo.delete(p.get());

        result.setMessage("successfully");
        result.setCode(200);
        result.setResult("delete profile id: " + p.get().getId() + " successfully");
        return ResponseEntity.ok(result);

    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Profile>> createNew(@RequestBody ProfileRequest profileRequest){

      Optional<User> u =  _iuserRepo.findById(profileRequest.getU_id());

        Profile p = new Profile();

        p.setUser( u.orElse(null) );
        p.setBio(profileRequest.getBio() );
        p.setHeight(profileRequest.getHeight());
        p.setUser(u.orElse(null));
        p.setLanguages(profileRequest.getLanguages());
        p.setInterests(profileRequest.getInterests());
        p.setRelationship_goals(profileRequest.getRelationship_goals());
        p.setAge(profileRequest.getAge());
        p.setDate_birth( profileRequest.getDate_birth() );
        p.setPassions(profileRequest.getPassions() );
        p.setAbout_me(profileRequest.getAbout_me() );
        p.setBasic(profileRequest.getBasic() );
        p.setLife_style(profileRequest.getLife_style() );
        u.get().setGender(profileRequest.getGender());
        /*
        *     private int age;

    private Date date_birth;
        * */

        _iprofileRepo.save(p);
        _iuserRepo.save(u.get());

        Optional<Profile> p_2 = _iprofileRepo.findByUser(u.orElse(null));

        ApiResponse<Profile> result = new ApiResponse<>();

        result.setMessage("successfully");
        result.setCode(200);
        result.setResult( p_2.orElse(null) );
        return ResponseEntity.ok(result);

    }



    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Profile>> update(@RequestBody ProfileUpdate profileUpdate){


        // tim id ->
        Optional<Profile> u_p = _iprofileRepo.findById(profileUpdate.getId());
        ApiResponse<Profile> result = new ApiResponse<>();

        if( u_p.isEmpty() ){
            result.setMessage("ERROR! NOT FOUND");
            result.setCode(404);
            return ResponseEntity.ok(result);
        }

        if (profileUpdate.getBio() != null) u_p.get().setBio(profileUpdate.getBio());
        if (profileUpdate.getHeight() != null) u_p.get().setHeight(profileUpdate.getHeight());
        if (profileUpdate.getLanguages() != null) u_p.get().setLanguages(profileUpdate.getLanguages());
        if (profileUpdate.getInterests() != null) u_p.get().setInterests(profileUpdate.getInterests());
        if (profileUpdate.getRelationship_goals() != null) u_p.get().setRelationship_goals(profileUpdate.getRelationship_goals());
        if( profileUpdate.getAge() > 0 ) u_p.get().setAge(profileUpdate.getAge() );
        if( profileUpdate.getDate_birth() != null ) u_p.get().setDate_birth( profileUpdate.getDate_birth() );
        if( profileUpdate.getPassions() != null ) u_p.get().setPassions(profileUpdate.getPassions() );
        if( profileUpdate.getAbout_me() != null ) u_p.get().setAbout_me(profileUpdate.getAbout_me() );
        if( profileUpdate.getBasic() != null ) u_p.get().setBasic(profileUpdate.getBasic() );
        if(profileUpdate.getLife_style() != null) u_p.get().setLife_style(profileUpdate.getLife_style() );

        _iprofileRepo.save(u_p.get());

        result.setMessage("successfully");
        result.setCode(200);
        result.setResult(u_p.get());
        return ResponseEntity.ok(result);

    }

    // hãm handle image

    @PostMapping("/images")
    public ResponseEntity<ApiResponse<String>> postImages(@RequestParam int userId, @ModelAttribute List<MultipartFile> images){

        List<String> urls =  _messageFireBaseService.uploadFiles(images);
        Optional<User> u =  _iuserRepo.findById(userId);

        StringBuilder many_urls = new StringBuilder();
        for( String url : urls ){
            many_urls.append(url).append(", ");
        }

        // xoá ký tự cuoi:
        if( many_urls.length() > 0 ){
            many_urls.setLength( many_urls.length() - 2 );
        }

        u.get().setImages( many_urls.toString() );

        _iuserRepo.save( u.get() );

        System.out.println( many_urls.toString() );

        ApiResponse<String> result = new ApiResponse<>();

        result.setMessage("successfully");
        result.setCode(200);
        result.setResult(u.get().getImages());
        return ResponseEntity.ok(result);
    }

    // Upload images -> delete all old images
    @PostMapping("/update/images")
    public ResponseEntity<ApiResponse<String>> updateImages(@RequestParam int userId, @ModelAttribute List<MultipartFile> images) {
        Optional<User> u =  _iuserRepo.findById(userId);

        if( u.get().getImages() != null ){
            try{
                    _messageFireBaseService.deleteFiles(u.get().getImages());
            }catch (Exception ex){
                throw new RuntimeException(ex.getMessage());
            }
        }

        List<String> urls =  _messageFireBaseService.uploadFiles(images);
        StringBuilder many_urls = new StringBuilder();
        for( String url : urls ){
            many_urls.append(url).append(", ");
        }

        // xoá ký tự cuoi:
        if( many_urls.length() > 0 ){
            many_urls.setLength( many_urls.length() - 2 );
        }

        u.get().setImages( many_urls.toString() );

        _iuserRepo.save( u.get() );


        ApiResponse<String> result = new ApiResponse<>();

        result.setMessage("successfully");
        result.setCode(200);
        result.setResult("update images successfully");
        return ResponseEntity.ok(result);
    }


    @PutMapping("/update/images/web")
    public ResponseEntity<ApiResponse<String>> updateImages2(@RequestParam int userId, @ModelAttribute MultipartFile image){

        if(image == null) return null;
        Optional<User> u =  _iuserRepo.findById(userId);
        String a =  _messageFireBaseService.uploadFile(image);
       
        ApiResponse<String> result = new ApiResponse<>();

        result.setMessage("successfully");
        result.setCode(200);

       if( u.get().getImages() == null ){
           u.get().setImages( a );
           result.setResult(a );
           return ResponseEntity.ok(result);
       }

       StringBuilder manyUrls = new StringBuilder(u.get().getImages());

       manyUrls.append(", ").append(a);

       u.get().setImages( manyUrls.toString() );
        _iuserRepo.save(u.get());
        result.setResult(manyUrls.toString() );
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete/image/web")
    public ResponseEntity<ApiResponse<String>> deleteImage(@RequestBody DeleteImageDTO deleteImageDTO){

        Optional<User> u = _iuserRepo.findById(deleteImageDTO.getIdUser());
        ApiResponse<String> result = new ApiResponse<>();

        result.setMessage("successfully");
        result.setCode(200);

        if( u.get().getImages() == null ){
            return null;
        }

        List<String> urlList = new ArrayList<>(Arrays.asList( u.get().getImages().split(", ") ));
        urlList.remove(deleteImageDTO.getUrl());

        u.get().setImages( String.join(", ", urlList) );
        _iuserRepo.save(u.get());

        try{
            _messageFireBaseService.deleteFiles(deleteImageDTO.getUrl());
        }catch (Exception ex){
            throw new RuntimeException(ex.getMessage());
        }
        result.setResult(String.join(", ", urlList));
        return ResponseEntity.ok(result);
    }



        @PutMapping("/update/user")
    public ResponseEntity<ApiResponse<String>> updateUser(@RequestBody UpdateRequestProfile updateRequestProfile){

        Optional<User> u = _iuserRepo.findById( updateRequestProfile.getId() );

        ApiResponse<String> result = new ApiResponse<>();

        if ( u.isEmpty() ){
            result.setMessage("ERROR");
            result.setCode(404);
            result.setResult( "data not found" );
            return ResponseEntity.ok(result);
        }

        if( updateRequestProfile.getFullname() != null ) u.get().setFullname(updateRequestProfile.getFullname());
        if( updateRequestProfile.getPhone_number() != null ) u.get().setPhone_number(updateRequestProfile.getPhone_number() );
        if( updateRequestProfile.getLatitude() != null ) u.get().setLatitude(updateRequestProfile.getLatitude() );
        if( updateRequestProfile.getLongitude() != null ) u.get().setLongitude(updateRequestProfile.getLongitude() );
        if( updateRequestProfile.getGender() != null ) u.get().setGender(updateRequestProfile.getGender() );

        _iuserRepo.save(u.get());
        result.setMessage("Successfully");
        result.setCode(200);
        result.setResult( "Update successfully" );
        return ResponseEntity.ok(result);
    }


}
