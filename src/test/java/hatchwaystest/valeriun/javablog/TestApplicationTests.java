package hatchwaystest.valeriun.javablog;

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
				//.content(objectMapper.writeValueAsString(user)))
				.accept(MediaType.APPLICATION_JSON))
				//.andDo()
				.andExpect(status().isOk());
	}

	@Test
	void testForScience() throws Exception {
		mockMvc.perform(get("/api/posts")
				.param("tags", "science")
				//.content(objectMapper.writeValueAsString(user)))
				.accept(MediaType.APPLICATION_JSON))
				.andDo(new ResultHandler() {
					@Override
					public void handle(MvcResult mvcResult) throws Exception {
						String body = mvcResult.getResponse().getContentAsString();
						PostList list = new ObjectMapper().readValue(body, PostList.class);
						Assert.isTrue(list.getListOfPosts().get(0).getId() == 4, "first post should be with id 4");
					}
				})
				.andExpect(status().isOk());
		mockMvc.perform(get("/api/posts")
				.param("tags", "startups")
				//.content(objectMapper.writeValueAsString(user)))
				.accept(MediaType.APPLICATION_JSON))
				.andDo(new ResultHandler() {
					@Override
					public void handle(MvcResult mvcResult) throws Exception {
						String body = mvcResult.getResponse().getContentAsString();
						PostList list = new ObjectMapper().readValue(body, PostList.class);
						Assert.isTrue(list.getListOfPosts().get(0).getId() == 2, "first startup post should be with id 2");
					}
				})
				.andExpect(status().isOk());
	}
}
