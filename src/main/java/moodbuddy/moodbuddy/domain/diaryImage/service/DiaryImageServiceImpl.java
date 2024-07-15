package moodbuddy.moodbuddy.domain.diaryImage.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moodbuddy.moodbuddy.domain.diary.entity.Diary;
import moodbuddy.moodbuddy.domain.diaryImage.entity.DiaryImage;
import moodbuddy.moodbuddy.domain.diaryImage.repository.DiaryImageRepository;
import moodbuddy.moodbuddy.domain.profileImage.entity.ProfileImage;
import moodbuddy.moodbuddy.domain.profileImage.repository.ProfileImageRepository;
import moodbuddy.moodbuddy.domain.user.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class DiaryImageServiceImpl implements DiaryImageService {
    private final DiaryImageRepository diaryImageRepository;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.imgFolder}")
    private String imgFolder;

    @Override
    public void saveDiaryImages(List<MultipartFile> diaryImgList, Diary diary) throws IOException {
        if (diaryImgList != null && !diaryImgList.isEmpty()) {
            List<String> diaryUrlList = diaryImgList.stream()
                    .filter(imageFile -> imageFile != null && !imageFile.isEmpty())
                    .map(this::uploadImage)
                    .collect(Collectors.toList());

            saveDiaryImageEntities(diaryUrlList, diary);
        }
    }

    //파일 s3에 업로드

    private String uploadImage(MultipartFile diaryImg) {
        try {
            String originalFilename = diaryImg.getOriginalFilename();
            String filePath = imgFolder + "/" + originalFilename;

            amazonS3.putObject(bucket, filePath, diaryImg.getInputStream(), new ObjectMetadata());
            return amazonS3.getUrl(bucket, filePath).toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    private void saveDiaryImageEntities(List<String> diaryUrlList, Diary diary) {
        List<DiaryImage> diaryImages = diaryUrlList.stream()
                .map(url -> DiaryImage.builder()
                        .diaryImgURL(url)
                        .diary(diary)
                        .build())
                .collect(Collectors.toList());

        diaryImageRepository.saveAll(diaryImages);
    }

    @Override
    public void deleteDiaryImages(List<String> imagesToDelete) {
        for (String imageUrl : imagesToDelete) {
            try {
                deleteImageFromS3(imageUrl);
                deleteImageFromDatabase(imageUrl);
            } catch (IOException e) {
                log.error("Failed to delete image from S3", e);
                throw new RuntimeException("Failed to delete image from S3", e);
            }
        }
    }

    private void deleteImageFromS3(String imageUrl) throws IOException {
        String filePath = imgFolder + "/" + imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, filePath));
    }

    private void deleteImageFromDatabase(String imageUrl) {
        List<DiaryImage> diaryImages = diaryImageRepository.findByDiaryImgURL(imageUrl);
        for (DiaryImage diaryImage : diaryImages) {
            diaryImageRepository.delete(diaryImage);
        }
    }

    @Override
    public List<DiaryImage> findImagesByDiary(Diary diary) {
        Optional<List<DiaryImage>> optionalDiaryList = diaryImageRepository.findByDiary(diary);
        return optionalDiaryList.orElseGet(Collections::emptyList);
    }

    @Override
    public String saveProfileImages(MultipartFile newProfileImg) throws IOException {
            String url = uploadImage(newProfileImg);
            return url;
    }
}
