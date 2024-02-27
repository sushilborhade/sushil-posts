package com.sushil.gatewaydemo.post;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class PostDataLoader implements CommandLineRunner {
    public static final Logger log = LoggerFactory.getLogger(PostController.class);
    private final ObjectMapper objectMapper;
    private final PostRepository postRepository;

    public PostDataLoader(ObjectMapper objectMapper, PostRepository postRepository) {
        this.objectMapper = objectMapper;
        this.postRepository = postRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        if (postRepository.count() == 0) {
            String POSTS_JSON = "/data/posts.json";
            log.info("Loading posts into database from JSON: {}", POSTS_JSON);
            try (InputStream inputStream = TypeReference.class.getResourceAsStream(POSTS_JSON)) {
                Posts posts = objectMapper.readValue(inputStream, Posts.class);
                postRepository.saveAll(posts.posts());
            } catch (Exception e){
                throw new RuntimeException("Failed to read JSON data", e);
            }
            log.info("Data Loading for posts are done");
        }
    }
}
