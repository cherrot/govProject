/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ynu.domain;

import java.sql.Date;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author cherrot
 */
public class PostTest {

    public PostTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getBoardId method, of class Post.
     */
    @Test
    public void testGetBoardId() {
        System.out.println("getBoardId");
        Post instance = new Post();
        int expResult = 0;
        int result = instance.getBoardId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setBoardId method, of class Post.
     */
    @Test
    public void testSetBoardId() {
        System.out.println("setBoardId");
        int boardId = 0;
        Post instance = new Post();
        instance.setBoardId(boardId);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCreateTime method, of class Post.
     */
    @Test
    public void testGetCreateTime() {
        System.out.println("getCreateTime");
        Post instance = new Post();
        Date expResult = null;
        Date result = instance.getCreateTime();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setCreateTime method, of class Post.
     */
    @Test
    public void testSetCreateTime() {
        System.out.println("setCreateTime");
        Date createTime = null;
        Post instance = new Post();
        instance.setCreateTime(createTime);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPostId method, of class Post.
     */
    @Test
    public void testGetPostId() {
        System.out.println("getPostId");
        Post instance = new Post();
        int expResult = 0;
        int result = instance.getPostId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setPostId method, of class Post.
     */
    @Test
    public void testSetPostId() {
        System.out.println("setPostId");
        int postId = 0;
        Post instance = new Post();
        instance.setPostId(postId);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPostText method, of class Post.
     */
    @Test
    public void testGetPostText() {
        System.out.println("getPostText");
        Post instance = new Post();
        String expResult = "";
        String result = instance.getPostText();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setPostText method, of class Post.
     */
    @Test
    public void testSetPostText() {
        System.out.println("setPostText");
        String postText = "";
        Post instance = new Post();
        instance.setPostText(postText);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPostTitle method, of class Post.
     */
    @Test
    public void testGetPostTitle() {
        System.out.println("getPostTitle");
        Post instance = new Post();
        String expResult = "";
        String result = instance.getPostTitle();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setPostTitle method, of class Post.
     */
    @Test
    public void testSetPostTitle() {
        System.out.println("setPostTitle");
        String postTitle = "";
        Post instance = new Post();
        instance.setPostTitle(postTitle);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTopic method, of class Post.
     */
    @Test
    public void testGetTopic() {
        System.out.println("getTopic");
        Post instance = new Post();
        instance.getTopic();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setTopic method, of class Post.
     */
    @Test
    public void testSetTopic() {
        System.out.println("setTopic");
        Topic topic = null;
        Post instance = new Post();
        instance.setTopic(topic);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUser method, of class Post.
     */
    @Test
    public void testGetUser() {
        System.out.println("getUser");
        Post instance = new Post();
        instance.getUser();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setUser method, of class Post.
     */
    @Test
    public void testSetUser() {
        System.out.println("setUser");
        User user = null;
        Post instance = new Post();
        instance.setUser(user);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
