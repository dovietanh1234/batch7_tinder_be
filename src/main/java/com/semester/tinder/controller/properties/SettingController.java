package com.semester.tinder.controller.properties;

import com.semester.tinder.dto.request.Setting.SettingRequest;
import com.semester.tinder.dto.response.ApiResponse;
import com.semester.tinder.entity.Setting;
import com.semester.tinder.entity.User;
import com.semester.tinder.repository.ISettingRepo;
import com.semester.tinder.repository.IUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/public/setting")
public class SettingController {

    @Autowired
    private ISettingRepo _iSettingRepo;

    @Autowired
    private IUserRepo _iuserRepo;

    @GetMapping("/getId")
    public ResponseEntity<ApiResponse<Setting>> getProfile(@RequestParam int id){

        ApiResponse<Setting> result = new ApiResponse<>();

        Optional<User> u = _iuserRepo.findById(id);

        Optional<Setting> p = _iSettingRepo.findByUser(u.orElse(null));
        if( p.isEmpty() ){
            result.setMessage("error! data not found");
            result.setCode(404);
            return ResponseEntity.ok(result);
        }

        result.setMessage("successfully");
        result.setCode(200);
        result.setResult( p.get() );
        return ResponseEntity.ok(result);

    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<String>> deleteP(@RequestParam int id){

        ApiResponse<String> result = new ApiResponse<>();

        Optional<User> u = _iuserRepo.findById(id);

        Optional<Setting> p = _iSettingRepo.findByUser( u.orElse(null) );

        if( p.isEmpty() ){
            result.setMessage("error! data not found");
            result.setCode(404);
            return ResponseEntity.ok(result);
        }

        _iSettingRepo.delete(p.get());

        result.setMessage("successfully");
        result.setCode(200);
        result.setResult("delete profile id: " + p.get().getId() + " successfully");
        return ResponseEntity.ok(result);

    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Setting>> createNew(@RequestBody SettingRequest settingRequest){

        Optional<User> u_report =  _iuserRepo.findById(settingRequest.getUser_id());

        Setting new_re = new Setting();

        new_re.setLocation( settingRequest.getLocation() );
        new_re.setUser(u_report.orElse(null));
        new_re.setDistance_preference( settingRequest.getDistance_preference());
        new_re.setLooking_for(settingRequest.getLooking_for() );
        new_re.setMin_age_preference(settingRequest.getMin_age_preference());
        new_re.setMax_age_preference(settingRequest.getMax_age_preference());


        _iSettingRepo.save(new_re);

        ApiResponse<Setting> result = new ApiResponse<>();

        result.setMessage("successfully");
        result.setCode(200);
        result.setResult( new_re );
        return ResponseEntity.ok(result);

    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Setting>> update(@RequestBody SettingRequest settingRequest){


        // tim id ->
        Optional<Setting> u_p = _iSettingRepo.findById(settingRequest.getId());
        ApiResponse<Setting> result = new ApiResponse<>();

        if( u_p.isEmpty() ){
            result.setMessage("ERROR! NOT FOUND");
            result.setCode(404);
            return ResponseEntity.ok(result);
        }

        if (settingRequest.getLocation() != null) u_p.get().setLocation(settingRequest.getLocation());
        if (settingRequest.getDistance_preference() > 2) u_p.get().setDistance_preference(settingRequest.getDistance_preference());
        if (settingRequest.getLooking_for() != null) u_p.get().setLooking_for(settingRequest.getLooking_for());
        if (settingRequest.getMin_age_preference() > 16) u_p.get().setMin_age_preference(settingRequest.getMin_age_preference());
        if (settingRequest.getMax_age_preference() < 60) u_p.get().setMax_age_preference(settingRequest.getMax_age_preference());



        _iSettingRepo.save(u_p.get());

        result.setMessage("successfully");
        result.setCode(200);
        result.setResult(u_p.get());
        return ResponseEntity.ok(result);

    }


}
