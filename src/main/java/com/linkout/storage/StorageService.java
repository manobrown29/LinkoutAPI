package com.linkout.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class StorageService {
    private final S3Client s3Client;

    private final String bucket;

    private final String region;

    private final List<String> TIPOS_PERMITIDOS = List.of("image/png", "image/webp", "image/jpeg", "image/jpg");

    public StorageService(@Value("${app.s3.bucket}") String bucket,
                          @Value("${app.s3.region}") String region) {
        this.bucket = bucket;
        this.region = region;
        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .build();
    }

    public String upload(MultipartFile file, String folder){
        validate(file);
        String key = folder + "/" + UUID.randomUUID() + getExtension(file.getOriginalFilename());
        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(file.getContentType())
                    .build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        }catch (IOException e){
            throw new RuntimeException("Falha ao ler o arquivo ", e);
        }

        return "https//" + bucket + ".s3." +region+ ".amazon.com/" + key;
    }

    private void validate(MultipartFile arquivo){
        if (arquivo == null || arquivo.isEmpty()){
            throw new RuntimeException("Arquivo de imagem vazio ou ausente");
        }

        String tipoDoArquivo = arquivo.getContentType();

        if (tipoDoArquivo == null || !TIPOS_PERMITIDOS.contains(tipoDoArquivo)){
            throw new RuntimeException("Tipo não suportado envie, jpeg, png, jpg ou webp");
        }
    }

    private String getExtension(String nomeArquivo){
        if (nomeArquivo != null && nomeArquivo.contains(".")){
            return nomeArquivo.substring(nomeArquivo.lastIndexOf("."));
        }
        return "";
    }
}
