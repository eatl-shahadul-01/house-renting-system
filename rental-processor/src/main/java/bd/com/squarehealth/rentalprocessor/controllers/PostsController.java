package bd.com.squarehealth.rentalprocessor.controllers;

import bd.com.squarehealth.corelibrary.common.ApiException;
import bd.com.squarehealth.corelibrary.common.ApiResponse;
import bd.com.squarehealth.corelibrary.common.security.AuthenticatedUserData;
import bd.com.squarehealth.corelibrary.dtos.PostDto;
import bd.com.squarehealth.corelibrary.enumerations.PostStatus;
import bd.com.squarehealth.corelibrary.services.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/v{version}/posts")
public class PostsController {

    @Autowired
    private PostsService postsService;

    @GetMapping(path = "{postId}")
    public ApiResponse getCreatorsPostById(
            @PathVariable
            Long postId) throws Exception {
        AuthenticatedUserData authenticatedUserData = (AuthenticatedUserData) SecurityContextHolder.getContext().getAuthentication();
        PostDto post = postsService.findCreatorsPostById(postId, authenticatedUserData.getUserId());

        if (post == null) { throw new ApiException(HttpStatus.NOT_FOUND, "Requested post was not found."); }

        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "Post retrieved successfully.");
        apiResponse.setData("post", post);

        return apiResponse;
    }

    @GetMapping
    public ApiResponse getCreatorsPosts() {
        AuthenticatedUserData authenticatedUserData = (AuthenticatedUserData) SecurityContextHolder.getContext().getAuthentication();
        List<PostDto> posts = postsService.findCreatorsPosts(authenticatedUserData.getUserId());
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "Posts retrieved successfully.");
        apiResponse.setData("posts", posts);

        return apiResponse;
    }

    // ADMIN ONLY...
    @GetMapping(path = "pending")
    public ApiResponse getPendingPostsForAdmin() {
        List<PostDto> posts = postsService.findPostsByPostStatus(PostStatus.PENDING);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "Pending posts retrieved successfully.");
        apiResponse.setData("posts", posts);

        return apiResponse;
    }

    @PostMapping
    public ApiResponse createPost(
            @Valid
            @RequestBody
            PostDto postData) {
        AuthenticatedUserData authenticatedUserData = (AuthenticatedUserData) SecurityContextHolder.getContext().getAuthentication();
        postData.setPostedBy(authenticatedUserData.getUserId());

        PostDto post = postsService.createPost(postData);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "Post created successfully.");
        apiResponse.setData("post", post);

        return apiResponse;
    }

    // ADMIN ONLY...
    @PatchMapping(path = "{postId}/status/{status}")
    public ApiResponse changePostStatus(
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
