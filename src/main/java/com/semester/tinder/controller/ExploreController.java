package com.semester.tinder.controller;

import com.semester.tinder.dto.response.ApiResponse;
import com.semester.tinder.entity.Explore;
import com.semester.tinder.entity.Setting;
import com.semester.tinder.entity.User;
import com.semester.tinder.repository.IExploreRepo;
import com.semester.tinder.repository.ISettingRepo;
import com.semester.tinder.repository.IUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/public/explore")
public class ExploreController {

   @Autowired
   private IUserRepo _iUserRepo;

   @Autowired
   private ISettingRepo _iSettingRepo;

   @Autowired
   private IExploreRepo _iExploreRepo;

    // get về dữ liệu:
    @Transactional
    @GetMapping
        public ResponseEntity<ApiResponse<List<com.semester.tinder.entity.Explore>>> getExplore(@RequestParam int userId, @RequestParam int offset){

        Optional<User> user = _iUserRepo.findById(userId);

        Optional<Setting> setting = _iSettingRepo.findByUser( user.get() );


        ApiResponse<List<Explore>> result = new ApiResponse<>();

        result.setMessage("Successfully");
        result.setCode(200);
        result.setResult(_iExploreRepo.exploreUsers(userId, user.get().getLatitude(), user.get().getLongitude(), setting.get().getDistance_preference(), setting.get().getLooking_for(), setting.get().getMin_age_preference(), setting.get().getMax_age_preference(), offset));

        return ResponseEntity.ok(result);

    }

/*
* @Param("userId") int userId,
                             @Param("latitude") double latitude,
                             @Param("longitude") double longitude,
                             @Param("distance_preference") int distance_preference,
                             @Param("looking_for") String looking_for,
                             @Param("min_age_preference") int min_age_preference,
                             @Param("max_age_preference") int max_age_preference,
                             @Param("offset") int offset
* */

}
