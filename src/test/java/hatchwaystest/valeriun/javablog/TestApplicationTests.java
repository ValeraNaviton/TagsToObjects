package hatchwaystest.valeriun.javablog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hatchwaystest.valeriun.javablog.endpoints.QueryController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = QueryController.class)
@ContextConfiguration(classes = { TestConfiguration.class })

class TestApplicationTests {
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@BeforeEach
	public void setup() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}

	@Test
	void dummyTest() throws Exception {
		mockMvc.perform(get("/api/posts")
				.param("tags", "tech")
				.param("sortBy", "id")
				.param("direction", "asc")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	void testForScience() throws Exception {
		mockMvc.perform(get("/api/posts")
						.param("tags", "science")
						.accept(MediaType.APPLICATION_JSON))
				.andDo(new ResultHandler() {
					@Override
					public void handle(MvcResult mvcResult) throws Exception {
						PostList list = fromJson(mvcResult);
						Assert.isTrue(list.getListOfPosts().get(0).getId() == 1, "first post should be with id 1");
					}
				})
				.andExpect(status().isOk());
	}

	@Test
	void testForStartupsWithSorting() throws Exception {
		mockMvc.perform(get("/api/posts")
						.param("tags", "startups")
						.accept(MediaType.APPLICATION_JSON))
				.andDo(new ResultHandler() {
					@Override
					public void handle(MvcResult mvcResult) throws Exception {
						PostList list = fromJson(mvcResult);
						Assert.isTrue(list.getListOfPosts().get(0).getId() == 11, "first startup post should be with id 11");
					}
				})
				.andExpect(status().isOk());

		mockMvc.perform(get("/api/posts")
						.param("tags", "startups")
						.param("direction", "desc")
						.accept(MediaType.APPLICATION_JSON))
				.andDo(new ResultHandler() {
					@Override
					public void handle(MvcResult mvcResult) throws Exception {
						PostList list = fromJson(mvcResult);
						Assert.isTrue(list.getListOfPosts().get(0).getId() == 13, "last startup post should be with id 13");
					}
				})
				.andExpect(status().isOk());
	}

	@Test
	void testDedup() throws Exception {
		mockMvc.perform(get("/api/posts")
						.param("tags", "startups, math, science")
						.accept(MediaType.APPLICATION_JSON))
				.andDo(new ResultHandler() {
					@Override
					public void handle(MvcResult mvcResult) throws Exception {
						PostList list = fromJson(mvcResult);
						Set<Post> dedupedPosts = new HashSet<>(list.getListOfPosts());
						Assert.isTrue(list.getListOfPosts().size() == dedupedPosts.size(), "no duplicates should come");
					}
				})
				.andExpect(status().isOk());
	}

	@Test
	void testEmptyTag() throws Exception {
		mockMvc.perform(get("/api/posts")
						.param("tags", "beauty")
						.accept(MediaType.APPLICATION_JSON))
				.andDo(new ResultHandler() {
					@Override
					public void handle(MvcResult mvcResult) throws Exception {
						PostList list = fromJson(mvcResult);
						Assert.isTrue(list.getListOfPosts().isEmpty(), "no beauty items are expected for test loader");
					}
				})
				.andExpect(status().isOk());
	}

	@Test
	void testHashAndEquals(){
		Post post1 = new Post("Elon Musk", 11, 11, 2, 1.0, 1, Arrays.asList("startups", "technology"));
		Post post2 = new Post("Elon2 Musk2", 12, 11, 3, 1.1, 2, Arrays.asList("Z1", "Z2"));
		Assert.isTrue(post1.equals(post2));
		Assert.isTrue(post1.hashCode() == post2.hashCode());
		post1 = new Post("Elon Musk", 11, 11, 2, 1.0, 1, Arrays.asList("startups", "technology"));
		post2 = new Post("Elon Musk", 11, 12, 2, 1.0, 1, Arrays.asList("startups", "technology"));
		Assert.isTrue(!post1.equals(post2));
		Assert.isTrue(post1.hashCode() != post2.hashCode());
	}

	private PostList fromJson(MvcResult mvcResult) throws UnsupportedEncodingException, JsonProcessingException {
		String body = mvcResult.getResponse().getContentAsString();
		return new ObjectMapper().readValue(body, PostList.class);
	}
}
