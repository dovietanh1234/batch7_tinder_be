package com.semester.tinder.service.message;


import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.cloud.storage.Blob;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.cloud.StorageClient;
import com.semester.tinder.dto.firebase.GetMessagedto;
import com.semester.tinder.dto.firebase.Message;
import com.semester.tinder.dto.firebase.MessageFormCreate;
import com.semester.tinder.dto.request.RabbitMQ.Notification;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class MessageFirebaseService {

    /*
    * Document_id -> chinh la id_matches
    * */
public String createMessage(Message message) {
try {
    Firestore dbFirestore = FirestoreClient.getFirestore();

    // ở đoạn code này có thể gay loi vi -> dx tuong chua dc tao ma ta da cap nhat lai gia tri -> gay error
    ApiFuture<WriteResult> collectionApiFutre = dbFirestore.collection("message").document(message.getDocument_id()).set(message);

//    DocumentReference docRef = dbFirestore.collection("Message").document(message.getDocument_id());
//    FieldValue increment = FieldValue.increment(1);
//    docRef.update("id", increment);

    return collectionApiFutre.get().getUpdateTime().toString();
    // Cach khac phuc: su dung Firestore transaction -> confirm for set value & update id at the same time( primitive way )
}catch (Exception e){
    return e.getMessage();
}

}

    public String createNotify(Notification notification) {
        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();

            // ở đoạn code này có thể gay loi vi -> dx tuong chua dc tao ma ta da cap nhat lai gia tri -> gay error
            ApiFuture<WriteResult> collectionApiFutre = dbFirestore.collection("notification").document(notification.getId()).set(notification);

//    DocumentReference docRef = dbFirestore.collection("Message").document(message.getDocument_id());
//    FieldValue increment = FieldValue.increment(1);
//    docRef.update("id", increment);

            return collectionApiFutre.get().getUpdateTime().toString();
            // Cach khac phuc: su dung Firestore transaction -> confirm for set value & update id at the same time( primitive way )
        }catch (Exception e){
            return e.getMessage();
        }

    }

    // XỬ LÝ LẤY TIN NHĂN REAL
    public List<com.semester.tinder.dto.request.Test.Message> getMessages(GetMessagedto getMessagedto) throws ExecutionException, InterruptedException {

        Firestore dbFirestore = FirestoreClient.getFirestore();

        CollectionReference collectionReference = dbFirestore.collection("TestMessage");

        ApiFuture<QuerySnapshot> future =collectionReference.orderBy("timeSent", Query.Direction.DESCENDING).whereEqualTo("matchId", getMessagedto.getMatches_id()).offset(getMessagedto.getOffset()).limit(getMessagedto.getLimit()).get();//

        QuerySnapshot querySnapshot = future.get();

        List<com.semester.tinder.dto.request.Test.Message> messages = new ArrayList<>();

        if ( !querySnapshot.isEmpty() ){
            for ( DocumentSnapshot documentSnapshot : querySnapshot.getDocuments() ){
                messages.add(documentSnapshot.toObject(com.semester.tinder.dto.request.Test.Message.class));
            }
        }

        return messages;
    }

    // lấy ra danh sách notify chưa đoc

    // update các trường thành đã đọc


    public String createSocketMessage(com.semester.tinder.dto.request.Test.Message message) throws ExecutionException, InterruptedException {

    Firestore dbFirestore = FirestoreClient.getFirestore();
    message.setId( UUID.randomUUID().toString() );
    ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection("TestMessage").document( message.getId() ).set(message);
    return collectionApiFuture.get().getUpdateTime().toString();
}



// lay ra cac tin nhan co id_matches = 5
public List<Message> getListM(GetMessagedto messagedto) throws ExecutionException, InterruptedException {

    Firestore dbFirestore = FirestoreClient.getFirestore();

    CollectionReference collectionReference = dbFirestore.collection("Message");

    ApiFuture<QuerySnapshot> future =collectionReference.orderBy("sent_time", Query.Direction.DESCENDING).whereEqualTo("matches_id", messagedto.getMatches_id()).limit(20).get();

    QuerySnapshot querySnapshot = future.get();

    List<Message> messages = new ArrayList<>();

    if ( !querySnapshot.isEmpty() ){
        for ( DocumentSnapshot documentSnapshot : querySnapshot.getDocuments() ){
            messages.add(documentSnapshot.toObject(Message.class));
        }
    }

        return messages;
}


// LẤY THÔNG BÁO GIỐNG NHƯ TIN NHẮN:  REAL
public List<Notification> getListNotify( int receiverId ) throws ExecutionException, InterruptedException {

    Firestore dbFirestore = FirestoreClient.getFirestore();
    CollectionReference collectionReference = dbFirestore.collection("notification");

    ApiFuture<QuerySnapshot> future =collectionReference.orderBy("timeNotify", Query.Direction.DESCENDING).whereEqualTo("receiverId", receiverId).limit(10).get();

    QuerySnapshot querySnapshot = future.get();

    List<Notification> notify = new ArrayList<>();

    if ( !querySnapshot.isEmpty() ){

        for ( DocumentSnapshot documentSnapshot : querySnapshot.getDocuments() ){
            notify.add( documentSnapshot.toObject(Notification.class) );
        }
    }

    return notify;
}

// update tin nhắn đã đọc:
    public String updateMessageStatus(int matchId) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference collectionReference = dbFirestore.collection("TestMessage");

        ApiFuture<QuerySnapshot> future = collectionReference
                .whereEqualTo("matchId", matchId)
                .whereEqualTo("status", "SENT")
                .get();

        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = future.get();
        } catch (InterruptedException | ExecutionException e) {
            return "Error! has error occur";
        }

        if( !querySnapshot.isEmpty() ){
            for( DocumentSnapshot documentSnapshot : querySnapshot.getDocuments() ){
                // cập nhật lại status
                documentSnapshot.getReference().update("status", "SEEN");
            }
        }

        return "update successfully";
    }


// update thông báo đã đọc:

    public String updateNotifyStatus(int receiverId) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference collectionReference = dbFirestore.collection("notification");

        ApiFuture<QuerySnapshot> future = collectionReference
                .whereEqualTo("receiverId", receiverId)
                .whereEqualTo("read", false)
                .get();

        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = future.get();
        } catch (InterruptedException | ExecutionException e) {
            return "Error! has error occur";
        }

        if( !querySnapshot.isEmpty() ){
            for( DocumentSnapshot documentSnapshot : querySnapshot.getDocuments() ){
                // cập nhật lại status
                documentSnapshot.getReference().update("read", true);
            }
        }

        return "update successfully";
    }





public String deleteM( String document_id ){
    Firestore dbFirestore = FirestoreClient.getFirestore();
    ApiFuture<WriteResult> writeResult =dbFirestore.collection("Message").document(document_id).delete();
    return "delete success";
}

// XỬ LÝ XOÁ TIN NHĂN REAL
public String deleteMessage( String document_id ){
    Firestore dbFirestore = FirestoreClient.getFirestore();
    ApiFuture<WriteResult> writeResult =dbFirestore.collection("TestMessage").document(document_id).delete();
    return "delete success";
}

    public String deleteNotify( String document_id ){
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult =dbFirestore.collection("notification").document(document_id).delete();
        return "delete success";
    }


    public String uploadFile(MultipartFile multipartFile){
        try {
            InputStream is = multipartFile.getInputStream();
            String filename = multipartFile.getOriginalFilename();

            // upload to fire storage:
            StorageClient storageClient = StorageClient.getInstance();

            Blob blob = storageClient.bucket().create(filename, is, "image/jpeg");

            // lấy về đường dẫn url:
            String bucketName = blob.getBucket();
            String blobName = blob.getName();
            return String.format("https://storage.googleapis.com/%s/%s", bucketName, blobName);


        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<String> uploadFiles( List<MultipartFile> multipartFiles ){
        List<String> urls = new ArrayList<>();

        for( MultipartFile multipartFile : multipartFiles ){
           String url = uploadFile(multipartFile);
           if(url != null){
               urls.add(url);
           }
        }
        return urls;
    }

    // trả về 1 ảnh đầu tiền
    public String AnImage(String images){
      String[] parts = images.split(", ");
      return parts[0];
    }



    public void deleteFiles(String images){
        String[] parts = images.split(", ");

        for( String part : parts ){
            deleteUrl(part);
        }

    }

    public void deleteUrl(String fileUrl){

        try {
            // get reference to firebase storage:
            StorageClient storageClient = StorageClient.getInstance();

            // extract the file name from url:
            String filename = fileUrl.substring( fileUrl.lastIndexOf("/")+1 );

            // delete name file:
            storageClient.bucket().get(filename).delete();
            return;
        }catch (Exception e){
            return;
        }

    }




}
