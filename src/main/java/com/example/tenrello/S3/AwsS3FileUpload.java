package com.example.tenrello.S3;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public interface AwsS3FileUpload {
    /**
     * 파일 전환 후 업로드
     *
     * @param multipartFile 업로드 할 파일
     * @param dirName       업로드 할 S3 버킷 내의 디렉토리
     * @return S3 url 주소
     */
    String upload(MultipartFile multipartFile, String dirName) throws IOException;

    /**
     * S3로 파일 전달 (읽기 권한)
     *
     * @param uploadFile 전달할 파일
     * @param fileName   전달할 파일 이름
     * @return S3 url 주소
     */
    String putS3(File uploadFile, String fileName);

    /**
     * 전환하며 로컬에 생성된 File 삭제
     *
     * @param targetFile 삭제할 File
     */
    void removeNewFile(File targetFile);

    /**
     * MultipartFile 을 전달받아 S3로 전달 가능한 형태인 File 생성 (로컬에 저장)
     *
     * @param file 전달받은 MultipartFile
     * @return S3로 전달 가능한 File
     */
    Optional<File> convert(MultipartFile file) throws IOException;
}
