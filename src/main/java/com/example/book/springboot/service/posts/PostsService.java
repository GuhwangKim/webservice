package com.example.book.springboot.service.posts;

import com.example.book.springboot.domain.posts.Posts;
import com.example.book.springboot.domain.posts.PostsRepository;
import com.example.book.springboot.web.dto.posts.PostsListResponseDto;
import com.example.book.springboot.web.dto.posts.PostsResponseDto;
import com.example.book.springboot.web.dto.posts.PostsSaveRequestDto;
import com.example.book.springboot.web.dto.posts.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {

    //등록
    private final PostsRepository postsRepository;
    @Transactional//여러기능을 하나로 묶어 실행해서 하나라도 잘못되면 모두 취소해야한다(데이터 무결성 보장).
    public long save(PostsSaveRequestDto requestDto){
        return postsRepository.save(requestDto.toEntity()).getPnum();
    }

    // 수정
    @Transactional
    public Long update(Long pnum, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(pnum).orElseThrow(()->
                new IllegalArgumentException("해당 게시글이 없습니다. pnum = "+pnum));
        posts.update(requestDto.getTitle(), requestDto.getContent());
        return pnum;
    }

    //전체 조회
    @Transactional
    public List<PostsListResponseDto> findAllDesc(){
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }


    // 상세
    public PostsResponseDto findById(Long pnum) {
        Posts entity = postsRepository.findById(pnum)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다 id = "+pnum));
        return new PostsResponseDto(entity);
    }

    @Transactional
    public void delete(Long pnum) {
        Posts posts = postsRepository.findById(pnum)
                .orElseThrow(()->new IllegalArgumentException("해당 게시글이 없습니다 id = "+pnum)
                        );
        postsRepository.delete(posts);
    }
}
