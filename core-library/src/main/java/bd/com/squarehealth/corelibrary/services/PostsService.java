package bd.com.squarehealth.corelibrary.services;

import bd.com.squarehealth.corelibrary.dtos.PostDto;
import bd.com.squarehealth.corelibrary.enumerations.PostStatus;

import java.util.List;

public interface PostsService {

    /**
     * Retrieves post by ID and the ID of the post creator.
     * @param postId ID of the post.
     * @param postedBy ID of the post creator.
     * @return Returns the post if exists. Otherwise, returns null.
     * Note: If the ID of the post creator does not match, this method returns null.
     */
    PostDto findCreatorsPostById(Long postId, Long postedBy);

    /**
     * Retrieves all the posts of a post creator.
     * @param postedBy ID of the post creator.
     * @return Returns a list of posts. If none found, returns an empty list.
     */
    List<PostDto> findCreatorsPosts(Long postedBy);

    /**
     * Retrieves all the posts that are of provided post status.
     * @param postStatus Status to match posts.
     * @return Returns a list of posts. If none found, returns an empty list.
     */
    List<PostDto> findPostsByPostStatus(PostStatus postStatus);

    /**
     * Changes the status of a post by ID.
     * @param postId ID of the post.
     * @param status Status that shall be assigned to the post .
     * @return Returns the updated post. If none found, returns null.
     */
    PostDto changePostStatusById(Long postId, PostStatus status);

    /**
     * Creates a new rental post.
     * @param postData Necessary data for creating a post.
     * @return Returns the created post.
     */
    PostDto createPost(PostDto postData);
}
