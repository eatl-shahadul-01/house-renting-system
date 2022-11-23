package bd.com.squarehealth.corelibrary.services;

import bd.com.squarehealth.corelibrary.dtos.PostDto;
import bd.com.squarehealth.corelibrary.entities.House;
import bd.com.squarehealth.corelibrary.enumerations.PostStatus;
import bd.com.squarehealth.corelibrary.repositories.HouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostsServiceImpl implements PostsService {

    @Autowired
    private HouseRepository houseRepository;

    private <Type> List<PostDto> toPosts(List<Type> list) {
        List<PostDto> posts = new ArrayList<>(list.size());

        for (Type element : list) {
            posts.add(new PostDto(element));
        }

        return posts;
    }

    @Override
    public PostDto findCreatorsPostById(Long postId, Long postedBy) {
        Optional<House> optionalHouse = houseRepository.findByIdAndPostedBy(postId, postedBy);

        if (!optionalHouse.isPresent()) { return null; }

        PostDto postData = new PostDto(optionalHouse.get());

        return postData;
    }

    @Override
    public List<PostDto> findCreatorsPosts(Long postedBy) {
        List<House> houses = houseRepository.findByPostedBy(postedBy);
        List<PostDto> posts = toPosts(houses);

        return posts;
    }

    @Override
    public List<PostDto> findPostsByPostStatus(PostStatus postStatus) {
        List<House> houses = houseRepository.findByPostStatus(postStatus);
        List<PostDto> posts = toPosts(houses);

        return posts;
    }

    @Override
    public PostDto changePostStatusById(Long postId, PostStatus status) {
        Optional<House> optionalPost = houseRepository.findById(postId);

        if (!optionalPost.isPresent()) { return null; }

        House house = optionalPost.get();
        house.setPostStatus(status);

        houseRepository.save(house);

        PostDto postData = new PostDto(house);

        return postData;
    }

    @Override
    public PostDto createPost(PostDto postData) {
        House house = postData.toObject(House.class);
        house.setPostStatus(PostStatus.PENDING);
        house = houseRepository.save(house);

        PostDto createdPostData = new PostDto(house);

        return createdPostData;
    }
}
