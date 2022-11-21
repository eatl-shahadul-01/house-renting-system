package bd.com.squarehealth.rentalprocessor.controllers;

import bd.com.squarehealth.corelibrary.common.ApiException;
import bd.com.squarehealth.corelibrary.common.ApiResponse;
import bd.com.squarehealth.rentalprocessor.dtos.PostDto;
import bd.com.squarehealth.rentalprocessor.enumerations.PostStatus;
import bd.com.squarehealth.rentalprocessor.services.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/v{version}/posts")
public class PostsController {

    @Autowired
    private PostsService postsService;

    @GetMapping(path = "{postId}")
    public ApiResponse getCreatorsHouseById(
            @PathVariable
            Long postId) throws Exception {
        PostDto post = postsService.findCreatorsPostById(postId, 5L);

        if (post == null) { throw new ApiException(HttpStatus.NOT_FOUND, "Requested post was not found."); }

        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "Post retrieved successfully.");
        apiResponse.setData("post", post);

        return apiResponse;
    }

    @GetMapping
    public ApiResponse getCreatorsHouses() {
        List<PostDto> posts = postsService.findCreatorsPosts(5L);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "Posts retrieved successfully.");
        apiResponse.setData("posts", posts);

        return apiResponse;
    }

    // ADMIN ONLY...
    @GetMapping(path = "pending")
    public ApiResponse getPendingHousesForAdmin() {
        List<PostDto> posts = postsService.findPostsByPostStatus(PostStatus.PENDING);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "Pending posts retrieved successfully.");
        apiResponse.setData("houses", posts);

        return apiResponse;
    }

    @PostMapping
    public ApiResponse createPost(
            @Valid
            @RequestBody
            PostDto postData) {
        PostDto post = postsService.createPost(postData);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "Post created successfully.");
        apiResponse.setData("post", post);

        return apiResponse;
    }

    // ADMIN ONLY...
    @PatchMapping(path = "{postId}/status/{status}")
    public ApiResponse changeHouseStatus(
            @PathVariable
            Long postId,
            @PathVariable
            PostStatus status) {
        PostDto post = postsService.changePostStatusById(postId, status);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "Successfully updated post status to " + status.toString().toLowerCase() + ".");
        apiResponse.setData("post", post);

        return apiResponse;
    }
}
