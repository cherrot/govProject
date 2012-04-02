/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package ynu.web.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.unitils.spring.annotation.SpringBeanByType;

import ynu.util.CommonConstant;
import ynu.dao.Page;
import ynu.domain.Board;
import ynu.domain.Post;
import ynu.domain.Topic;
import ynu.domain.User;
import ynu.service.ForumService;
import ynu.web.BoardManageController;


public class BoardManageControllerTest  extends BaseWebTest {
	@Autowired
	private ForumService forumService;
	@SpringBeanByType
	private BoardManageController controller;

	@Test
	public void listBoardTopics() throws Exception {
		request.setRequestURI("/board/listBoardTopics-1");
		request.addParameter("boardId", "1"); // ⑥ 设置请求URL及参数
		request.setMethod("GET");
		ModelAndView mav = handlerAdapter.handle(request, response, controller);
		Board board = (Board) request.getAttribute("board");
		assertNotNull(mav);
		assertEquals(mav.getViewName(), "/listBoardTopics");
		assertNotNull(board);
		assertThat(board.getBoardName(), equalTo("育儿"));// ⑧ 验证返回结果
		assertThat(board.getTopicNum(), greaterThan(1));
	}


	@Test
	public void addTopicPage() throws Exception {
		request.setRequestURI("/board/addTopicPage-1");
		request.addParameter("boardId", "1"); // ⑥ 设置请求URL及参数
		request.setMethod("GET");
		ModelAndView mav = handlerAdapter.handle(request, response, controller);
		assertNotNull(mav);
		assertEquals(mav.getViewName(), "/addTopic");
	}

	@Test
	public void addTopic()throws Exception  {
		request.setRequestURI("/board/addTopic");
		request.addParameter("boardId", "1");
		request.setMethod("POST");

		User user = new User();
		user.setUserId(1);
		user.setUserName("tom");
		user.setPassword("1234");
		request.getSession().setAttribute(CommonConstant.USER_CONTEXT, user);
		session.setAttribute(CommonConstant.USER_CONTEXT, user);

		request.addParameter("topicTitle", "育儿经验");
		request.addParameter("mainPost.postTitle", "育儿经验");
		request.addParameter("mainPost.postText", "育儿经验交流！！");

		ModelAndView mav = handlerAdapter.handle(request, response, controller);
		assertNull(mav);
	}

	@Test
	public void listTopicPosts()throws Exception  {
		request.setRequestURI("/board/listTopicPosts-1");
		request.addParameter("topicId", "1");
		request.setMethod("GET");
		ModelAndView mav = handlerAdapter.handle(request, response, controller);

		Topic topic = (Topic) request.getAttribute("topic");
		Page pagedPost = (Page) request.getAttribute("pagedPost");

		assertNotNull(topic);
		assertNotNull(pagedPost);
		assertThat(pagedPost.getPageSize(), greaterThan(1));
		assertNotNull(mav);
		assertEquals(mav.getViewName(), "/listTopicPosts");
	}


	@Test
	public void addPost()throws Exception  {
		request.setRequestURI("/board/addPost");
		request.addParameter("boardId", "1");
		request.addParameter("topicId", "1");
		request.addParameter("postTitle", "育儿经验");
		request.addParameter("postText", "育儿经验交流！！");
		request.setMethod("POST");

		User user = new User();
		user.setUserId(1);
		user.setUserName("tom");
		user.setPassword("1234");
		request.getSession().setAttribute(CommonConstant.USER_CONTEXT, user);
		session.setAttribute(CommonConstant.USER_CONTEXT, user);

		handlerAdapter.handle(request, response, controller);
	}

	@Test
	public void removeBoard()throws Exception  {
		request.setRequestURI("/board/removeBoard");
		request.addParameter("boardIds", "5,6");
		request.setMethod("GET");
		handlerAdapter.handle(request, response, controller);
	}

	@Test
	public void removeTopic()throws Exception  {
		request.setRequestURI("/board/removeTopic");
		request.addParameter("topicIds", "6,7");
		request.setMethod("GET");
		handlerAdapter.handle(request, response, controller);
	}

	@Test
	public void makeDigestTopic()throws Exception  {
		request.setRequestURI("/board/makeDigestTopic");
		request.addParameter("topicIds", "1,2");
		request.setMethod("GET");

		handlerAdapter.handle(request, response, controller);
	}
}
