package com.urunner.khweb.service.lecture;

import com.urunner.khweb.controller.dto.lecture.DtoWrapper;
import com.urunner.khweb.controller.dto.lecture.LectureDto;
import com.urunner.khweb.controller.dto.lecture.LectureListDto;
import com.urunner.khweb.controller.dto.lecture.LectureVideoDto;
import com.urunner.khweb.entity.lecture.Lecture;
//import com.urunner.khweb.entity.lecture.LectureImage;
import com.urunner.khweb.entity.lecture.LectureList;
import com.urunner.khweb.entity.lecture.LectureVideo;
import com.urunner.khweb.entity.sort.Category;
import com.urunner.khweb.entity.sort.CategoryLecture;
import com.urunner.khweb.repository.lecture.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Transactional
@Service
public class LectureServiceImpl implements LectureService {


    @Autowired
    private LectureVideoRepository lectureVideoRepository;

    @Autowired
    private LectureListRepository lectureListRepository;

    @Autowired
    private LectureRepository lectureRepository;

//    @Autowired
//    private LectureImageRepository LectureImageRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @PersistenceContext
    private EntityManager em;

//    @Override
//    public void lectureVideo(LectureVideo lectureVideo, EnrollLectureVideoDto enrollLectureVideoDto) {
//
//        LectureList lectureList = lectureListRepository.enrollLectureList(enrollLectureVideoDto.writer, enrollLectureVideoDto.topic);
//
//        LectureVideo saveVideo = LectureVideo.builder()
//                .title(lectureVideo.getTitle())
//                .description(lectureVideo.getDescription())
//                .sequence(lectureVideo.getSequence())
//                .duration(lectureVideo.getDuration())
//                .build();
//
//        saveVideo.setLectureList(lectureList);
//
//        lectureVideoRepository.save(saveVideo);
//    }

    @Override
    public void lectureEnroll(Lecture lecture) {


        lectureRepository.save(lecture);
    }

    @Override
    public void lectureAddImage(String thum, String detail, Long id) {

        Lecture lecture = em.find(Lecture.class, id);

//        LectureImage lectureImage = LectureImage.builder()
//                .thumb_path(thum)
//                .detail_path(detail)
//                .build();
//
//        lectureImage.setLecture(lecture);
//
//        System.out.println(lectureImage.getThumb_path());

        lecture.setLectureThumb(thum);

        lecture.setLectureDetail(detail);

        lectureRepository.save(lecture);
    }

    @Override
    public void lectureRegister(String writer, String title, Long price, String desc, String categoryArray) {

        String[] category = categoryArray.split(",");

        List<String> cateList = new ArrayList();

        Lecture lecture = Lecture.builder()
                .writer(writer)
                .title(title)
                .price(price)
                .description(desc)
                .build();

        lectureRepository.save(lecture);

        for (String s : category) {
            cateList.add(s);
        }

        for (int i = 0; i < category.length; i++) {
            Category getCategory = categoryRepository.findByCategoryName(cateList.get(i));
            if (getCategory != null) {
                CategoryLecture categoryLecture = new CategoryLecture();
                categoryLecture.setLecture(lecture);
                categoryLecture.setCategory(getCategory);
                em.persist(categoryLecture);
            }
        }


    }

    @Override
    public void saveLectureSection(Long lectureId, String topic) {

        try {
            Lecture lecture = em.find(Lecture.class, lectureId);

            lecture.exist(lecture);

//            중복 나중에 한번에 하기로..
//            중복검증은 리스트 아이디, 토픽 두개를 가지로 체크 필요
//            if (lectureListRepository.findByTopic(topic).isPresent()) {
//                log.info("토픽이 중복이다..");
//                throw new Exception("중복 토픽입니다.");
//            }

            LectureList lectureList = LectureList.builder()
                    .topic(topic)
                    .build();

            lectureList.setLecture(lecture);
            lectureListRepository.save(lectureList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Page<LectureListDto> findAllLectureSection(Long lectureId) {

//       jpa페이징은 쿼리 -1개
        Lecture lecture = em.find(Lecture.class, lectureId);
//      나중에 페이징 처리하도록 둘것
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<LectureList> lecturePaging = lectureListRepository.findByLecture(lecture, pageRequest);

        Page<LectureListDto> pageDto = lecturePaging.map(m -> new LectureListDto(m.getLectureList_id(), m.getTopic(), m.getSection()));

        return pageDto;
    }

    @Override
    public void videoUpload(String title, String desc, String dur, Long id, String path) {

        LectureList lectureList = em.find(LectureList.class, id);

        LectureVideo lectureVideo = LectureVideo.builder()
                .title(title)
                .description(desc)
                .duration(dur)
                .videoPath(path)
                .build();

        lectureVideo.setLectureList(lectureList);

        lectureVideoRepository.save(lectureVideo);

    }

    @Transactional(readOnly = true)
    @Override
    public List<LectureDto> getLectureList(String writer) {

        List<Lecture> findAllLecture = lectureRepository.findByWriter(writer);
//      적합한 쿼리
        String query = "select c from Category c join CategoryLecture cl on cl.category = c " +
                "join Lecture l on l = cl.lecture where l.id = :lectureId";
//      부적합한 쿼리
//        String query = "select c from Category c join fetch c.lectureList cl " +
//                "join fetch cl.lecture l where l.id = :lectureId";

        return findAllLecture.stream().map(l ->
                new LectureDto(l.getLecture_id(), l.getWriter(), l.getTitle(),
                        l.getDescription(), l.getPrice(), l.isInProgress(),
                        l.isDiscounted(), l.getThumb_path(), l.getDetail_path(),
                        em.createQuery(query, Category.class)
                                .setParameter("lectureId", l.getLecture_id()).
                                getResultList()
                )).collect(Collectors.toList());
    }

    @Override
    public List<LectureVideoDto> findAllLectureVideo(Long lectureListId) {

        LectureList lectureList = em.find(LectureList.class, lectureListId);

        List<LectureVideo> findAllLectureVideo = lectureVideoRepository.findByLectureList(lectureList);

        return findAllLectureVideo.stream().map(l ->
                new LectureVideoDto(l.getId(), l.getTitle(), l.getDescription(), l.getSequence(), l.getDuration(),
                        l.getVideoPath())).collect(Collectors.toList());
    }

    @Override
    public Optional<LectureDto> getBasicInfo(Long lectureId) {

        Optional<Lecture> lecture = lectureRepository.findById(lectureId);

        Lecture lecture1 = em.find(Lecture.class, lectureId);

        String query = "select c from Category c join CategoryLecture cl on cl.category = c " +
                "join Lecture l on l = cl.lecture where l.id = :lectureId";

        return lecture.stream().map(l ->
                new LectureDto(l.getLecture_id(), l.getWriter(), l.getTitle(),
                        l.getDescription(), l.getPrice(), l.isInProgress(), l.isDiscounted(),
                        l.getThumb_path(), l.getDetail_path(),
                        em.createQuery(query, Category.class)
                                .setParameter("lectureId", l.getLecture_id()).
                                getResultList())).findAny();
    }

    @Override
    public void deleteThumbImg(Long lectureId) {

        Optional<Lecture> lecture = lectureRepository.findById(lectureId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        lecture.orElseThrow(() -> new NoSuchElementException());
//        ispresnet ifpresent 구분 주의
        lecture.filter(l -> authentication.getName().equals(l.getWriter()))
                .ifPresent(l -> {
                    l.setLectureThumb(null);
                    lectureRepository.save(l);
                });
//        테스트용 토큰없는경우(X)
//        lecture.ifPresent(l -> {
//                    l.setLectureThumb(null);
//                    lectureRepository.save(l);
//                });

//        파일 삭제 아직 미구현


    }

    @Override
    public void deleteDetailImg(Long lectureId) {

        Optional<Lecture> lecture = lectureRepository.findById(lectureId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        lecture.orElseThrow(() -> new NoSuchElementException());
//        ispresnet ifpresent 구분 주의
        lecture.filter(l -> authentication.getName().equals(l.getWriter()))
                .ifPresent(l -> {
                    l.setLectureDetail(null);
                    lectureRepository.save(l);
                });
//        파일 삭제 아직 미구현
    }

    @Override
    public void deleteLecture(Long lectureId) {
        Optional<Lecture> lecture = lectureRepository.findById(lectureId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        lecture.orElseThrow(() -> new NoSuchElementException());
//        ispresnet ifpresent 구분 주의
        lecture.filter(l -> authentication.getName().equals(l.getWriter()))
                .ifPresent(l -> {
                    l.setLectureDetail(null);
                    lectureRepository.deleteById(l.getLecture_id());
                });
//        파일 삭제 아직 미구현
    }

    @Override
    public void modifyLecture(LectureDto lectureDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional<Lecture> lecture = lectureRepository.findById(lectureDto.getId());

        lecture.filter(l -> authentication.getName().equals(l.getWriter()))
                .ifPresent(l -> {
                    l.setTitle(lectureDto.getTitle());
                    l.setPrice(lectureDto.getPrice());
                    l.setDescription(lectureDto.getDesc());
                    lectureRepository.save(l);
                });
    }

    @Override
    public DtoWrapper getSectionTopic(Long lectureListId) {

        Optional<LectureList> lectureList = lectureListRepository.findById(lectureListId);

//        인증인가 처리는 get쪽에서  필요가 전혀 없을듯 수정,삭제만 인증 인가
        Optional<LectureListDto> lectureListDto = lectureList.stream().findAny().map(l -> new LectureListDto(l.getLectureList_id(), l.getTopic(), l.getSection()));

        return new DtoWrapper(lectureList);

    }

    @Override
    public void deleteSection(Long lectureListId) {

        Optional<LectureList> lectureList = lectureListRepository.findById(lectureListId);

        lectureList.filter(l -> authentication().equals(l.getLecture().getWriter()))
                .ifPresent(l -> lectureListRepository.deleteById(l.getLectureList_id()));

    }

    @Override
    public void modifySectionTopic(LectureListDto lectureListDto) {

        Optional<LectureList> lectureList = lectureListRepository.findById(lectureListDto.lectureList_id);

        lectureList.filter(l -> authentication().equals(l.getLecture().getWriter()))
                .ifPresent(l -> {
                    l.setTopic(lectureListDto.topic);
                    lectureListRepository.save(l);
                });
    }

    @Override
    public DtoWrapper getLectureVideoInfo(Long videoId) {

        Optional<LectureVideo> lectureVideo = lectureVideoRepository.findById(videoId);

        Optional<LectureVideoDto> lectureVideoDto = lectureVideo.stream().findAny().map(l ->
                new LectureVideoDto(l.getId(), l.getTitle(), l.getDescription(), l.getSequence(), l.getDuration(), l.getVideoPath()));

//        Stream<LectureVideoDto> lectureVideoDtoStream = lectureVideo.stream().map(l ->
//                new LectureVideoDto(l.getId(), l.getTitle(), l.getDescription(), l.getSequence(), l.getDuration(), l.getVideoPath())
//        );

        return new DtoWrapper(lectureVideoDto);
    }

    @Override
    public void deleteLectureVideo(Long videoId) {

        Optional<LectureVideo> videoInfo = lectureVideoRepository.findById(videoId);

        videoInfo.filter(l -> authentication().equals(l.getLectureList().getLecture().getWriter()))
                .ifPresent(l -> lectureVideoRepository.deleteById(videoId));
    }

    private String authentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
