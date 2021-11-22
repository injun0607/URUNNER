package com.urunner.khweb.controller.lecture;


import com.urunner.khweb.controller.dto.lecture.*;
import com.urunner.khweb.repository.lecture.LectureRepository;
import com.urunner.khweb.service.lecture.LectureService;
import com.urunner.khweb.service.member.MemberService;
import com.urunner.khweb.service.mypage.MypageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/manageLecture")
@Slf4j
public class LectureManageController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MypageService mypageService;

    @Autowired
    private LectureService lectureService;

    @Autowired
    private LectureRepository lectureRepository;

    @PostMapping("/joinLecturer")
    public ResponseEntity<Boolean> joinLecturer(@RequestBody JoinInstructorDto joinInstructorDto) {

        return new ResponseEntity<Boolean>(memberService.joinInstructor(joinInstructorDto), HttpStatus.OK);
    }

    @GetMapping("/addToWish/{lectureId}")
    public ResponseEntity<Boolean> addToWish(@PathVariable("lectureId") Long lectureId) throws AuthenticationException {

        return new ResponseEntity<Boolean>(mypageService.lectureAddToWish(lectureId), HttpStatus.OK);

    }

    @GetMapping("/addToCart/{lectureId}")
    public ResponseEntity<Boolean> addToCart(@PathVariable("lectureId") Long lectureId) throws AuthenticationException {

        return new ResponseEntity<Boolean>(mypageService.lectureAddCart(lectureId), HttpStatus.OK);
    }

    @GetMapping("/addToWishInLecture/{lectureId}")
    public ResponseEntity<DtoWrapper3> addToWishInLecture(@PathVariable("lectureId") Long lectureId) throws AuthenticationException {

        return new ResponseEntity<DtoWrapper3>(mypageService.addToWishInLecture(lectureId), HttpStatus.OK);
    }

    @GetMapping("/getCartList")
    public DtoWrapper getCartList() {

        return lectureService.getCartList();
    }

    @GetMapping("/getWishList")
    public DtoWrapper getWishList() {

        return lectureService.getWishList();
    }

    @GetMapping("/mainCartList")
    public DtoWrapper mainCartList() {

        return lectureService.mainCartList(0);
    }
    @GetMapping("/mainWishList")
    public DtoWrapper mainWishList() {

        return lectureService.mainWishList(0);
    }

    @PostMapping("/mainSearch")
    public DtoWrapper mainSearch(@RequestBody SearchCondition searchCondition) {

        return new DtoWrapper(lectureRepository.searchPage(searchCondition, searchCondition.getPage()));
    }

    @PostMapping("/regStudentComment")
    public ResponseEntity<Boolean> regStudentComment(@RequestBody ReviewDto reviewDto) {

        return new ResponseEntity<Boolean>(lectureService.regStudentComment(reviewDto), HttpStatus.OK);
    }
}
