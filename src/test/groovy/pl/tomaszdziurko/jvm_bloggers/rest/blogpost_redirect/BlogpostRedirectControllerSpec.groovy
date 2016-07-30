package pl.tomaszdziurko.jvm_bloggers.rest.blogpost_redirect

import org.springframework.test.web.servlet.MockMvc
import pl.tomaszdziurko.jvm_bloggers.blog_posts.domain.BlogPost
import pl.tomaszdziurko.jvm_bloggers.blog_posts.domain.BlogPostRepository
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup
/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
class BlogpostRedirectControllerSpec extends Specification {

    private BlogPostRepository blogPostRepositoryMock = Mock(BlogPostRepository)
    private MockMvc mockMvc = standaloneSetup(new BlogpostRedirectController(blogPostRepositoryMock)).build()

    def "should redirect to blogpost url"() {
        given:
            final String uid = '1234'
            final String url = 'http://www.blog.com/post'
        and:
            blogPostRepositoryMock.findByUid(uid) >> Optional.of(new BlogPost(url: url))
        expect:
            mockMvc.perform(get("/blogpost/r/$uid"))
                .andExpect(status().isFound())
                .andExpect(header().string('Location', url))
    }

    def "should return 404 when blogpost does not exist"() {
        given:
            final String uid = '1234'
        and:
            blogPostRepositoryMock.findByUid(uid) >> Optional.empty()
        expect:
            mockMvc.perform(get("/blogpost/r/$uid"))
                .andExpect(status().isNotFound())
    }
}
