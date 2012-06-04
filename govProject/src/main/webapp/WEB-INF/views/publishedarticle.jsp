<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="header.jspf" %>
    </head>
    <body>
        <div id="publisaleftsidebar"></div>
        <table border="0" cellpadding="10">
         <tr>
           <td align="left">文章</td>
           </tr>   
           <tr>
           <td align="left">  所有文章</td>
           </tr>
           <tr>
           <td align="left">  写文章</td>
           </tr>
           <tr>
           <td align="left">评论</td>
           </tr>
           <tr>
           <td align="left">个人资料</td>
           </tr>
           <tr>
           <td align="left">工具</td>
           </tr>
        </table>
        <div id="publisabody">
            <div id="publisabody-content">
               <h2>撰写文章</h2>
                <form name="post" action="post.php" method="post" id="post">
                    <input type="hidden" id="_wpnonce" name="_wpnonce" value="de048a6c90" />
                    <input type="hidden" name="_wp_http_referer" value="/wp-admin/post-new.php" />
                    <input type="hidden" id="user-id" name="user_ID" value="2" />
                    <input type="hidden" id="hiddenaction" name="action" value="editpost" />
                    <input type="hidden" id="originalaction" name="originalaction" value="editpost" />
                    <input type="hidden" id="post_author" name="post_author" value="2" />
                    <input type="hidden" id="post_type" name="post_type" value="post" />
                    <input type="hidden" id="original_post_status" name="original_post_status" value="auto-draft" />
                    <input type="hidden" id="referredby" name="referredby" value="http://www.cherrot.com/wp-admin/" />
                    <input type="hidden" name="_wp_original_http_referer" value="http://www.cherrot.com/wp-admin/" />
                    <input type='hidden' id='auto_draft' name='auto_draft' value='1' /><input type='hidden' id='post_ID' name='post_ID' value='1672' /><input type="hidden" id="autosavenonce" name="autosavenonce" value="49336af153" /><input type="hidden" id="meta-box-order-nonce" name="meta-box-order-nonce" value="5f9631659c" /><input type="hidden" id="closedpostboxesnonce" name="closedpostboxesnonce" value="452c9fa8a8" />
                    <div id="poststuff" class="metabox-holder has-right-sidebar">
                        <div id="side-info-column" class="inner-sidebar">
                            <div id="side-sortables" class="meta-box-sortables">
                                <div id="submitdiv" class="postbox " >
                                    <div class="handlediv" title="点击以切换"><br /></div>
                                    <h3 class='hndle'><span>发布</span></h3>
                                    <div class="inside">
                                        <div class="submitbox" id="submitpost">
                                            <div id="minor-publishing">
                                                <div style="display:none;">
                                                    <p class="submit">
                                                        <input type="submit" name="save" id="save" class="button" value="保存"  />
                                                    </p>
                                                </div>
                                                <div id="misc-publishing-actions">
                                                    <div class="misc-pub-section misc-pub-section-last">
                                                        <label for="post_status">状态：</label>
                                                        <span id="post-status-display">
                                                            草稿
                                                        </span>
                                                    </div>
                                                    <div class="misc-pub-section " id="visibility">
                                                        公开度： 
                                                        <span id="post-visibility-display">公开</span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div id="major-publishing-actions">
                                                <div id="delete-action">
                                                    <a class="submitdelete deletion" href="http://www.cherrot.com/wp-admin/post.php?post=1672&amp;action=trash&amp;_wpnonce=f23bd0775e">
                                                        移至回收站</a>
                                                </div>    
                                                <div id="publishing-action">
                                                    <img src="http://www.cherrot.com/wp-admin/images/wpspin_light.gif" class="ajax-loading" id="ajax-loading" alt="" />
		                                    <input name="original_publish" type="hidden" id="original_publish" value="提请审批" />
                                                    <input type="submit" name="publish" id="publish" class="button-primary" value="提请审批" tabindex="5" accesskey="p"  />
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div id="categorydiv" class="postbox " >
                                    <div class="handlediv" title="点击以切换"><br /></div><h3 class='hndle'><span>分类目录</span></h3>
                                    <div class="inside">
                                        <div id="taxonomy-category" class="categorydiv">
                                            <ul id="category-tabs" class="category-tabs">
                                                <li class="tabs"><a href="#category-all" tabindex="3">所有分类目录</a></li>
                                                <li class="hide-if-no-js"><a href="#category-pop" tabindex="3">最常用</a></li>
                                            </ul>
                                            <div id="category-pop" class="tabs-panel" style="display: none;">
                                                <ul id="categorychecklist-pop" class="categorychecklist form-no-clear" >
                                                    <li id="popular-category-3" class="popular-category">
                                                        <label class="selectit">
                                                            <input id="in-popular-category-3" type="checkbox"  value="3" />
				Memo			</label>
                                                    </li>
                                                    <li id="popular-category-97" class="popular-category">
                                                        <label class="selectit">
                                                            <input id="in-popular-category-97" type="checkbox"  value="97" />
				Linux			</label>
                                                    </li>
                                                    <li id="popular-category-168" class="popular-category">
                                                        <label class="selectit">
                                                            <input id="in-popular-category-168" type="checkbox"  value="168" />
				Notes			</label>
                                                    </li>
                                                    <li id="popular-category-114" class="popular-category">
                                                        <label class="selectit">
                                                            <input id="in-popular-category-114" type="checkbox"  value="114" />
				OpenSrc			</label>
                                                    </li>
                                                    <li id="popular-category-99" class="popular-category">
                                                        <label class="selectit">
                                                            <input id="in-popular-category-99" type="checkbox"  value="99" />
				Web			</label>
                                                    </li>
                                                    <li id="popular-category-197" class="popular-category">
                                                        <label class="selectit">
                                                            <input id="in-popular-category-197" type="checkbox"  value="197" />
				Java			</label>
                                                    </li>
                                                    <li id="popular-category-160" class="popular-category">
                                                        <label class="selectit">
                                                            <input id="in-popular-category-160" type="checkbox"  value="160" />
				AI			</label>
                                                    </li>
                                                    <li id="popular-category-77" class="popular-category">
                                                        <label class="selectit">
                                                            <input id="in-popular-category-77" type="checkbox"  value="77" />
				News			</label>
                                                    </li>
                                                    <li id="popular-category-169" class="popular-category">
                                                        <label class="selectit">
                                                            <input id="in-popular-category-169" type="checkbox"  value="169" />
				Code			</label>
                                                    </li>
                                                    <li id="popular-category-96" class="popular-category">
                                                        <label class="selectit">
                                                            <input id="in-popular-category-96" type="checkbox"  value="96" />
				Qt/C++			</label>
                                                    </li>
                                                </ul>
                                            </div>
                                            <div id="category-all" class="tabs-panel">
                                                <input type='hidden' name='post_category[]' value='0' />			
                                                <ul id="categorychecklist" class="list:category categorychecklist form-no-clear">
                                                    <li id='category-160' class="popular-category">
                                                        <label class="selectit">
                                                            <input value="160" type="checkbox" name="post_category[]" id="in-category-160" /> AI
                                                        </label>
                                                    </li>
                                                    <li id='category-169' class="popular-category">
                                                        <label class="selectit">
                                                            <input value="169" type="checkbox" name="post_category[]" id="in-category-169" /> Code
                                                        </label>
                                                        <ul class='children'>
                                                            <li id='category-197' class="popular-category">
                                                                <label class="selectit">
                                                                    <input value="197" type="checkbox" name="post_category[]" id="in-category-197" /> Java
                                                                </label>
                                                            </li>
                                                            <li id='category-96' class="popular-category">
                                                                <label class="selectit">
                                                                    <input value="96" type="checkbox" name="post_category[]" id="in-category-96" /> Qt/C++
                                                                </label>
                                                            </li>
                                                        </ul>
                                                    </li>
                                                    <li id='category-3' class="popular-category">
                                                        <label class="selectit">
                                                            <input value="3" type="checkbox" name="post_category[]" id="in-category-3" /> Memo
                                                        </label>
                                                    </li>
                                                    <li id='category-77' class="popular-category">
                                                        <label class="selectit">
                                                            <input value="77" type="checkbox" name="post_category[]" id="in-category-77" /> News
                                                        </label>
                                                    </li>
                                                    <li id='category-168' class="popular-category">
                                                        <label class="selectit">
                                                            <input value="168" type="checkbox" name="post_category[]" id="in-category-168" /> Notes
                                                        </label>
                                                    </li>
                                                    <li id='category-114' class="popular-category">
                                                        <label class="selectit">
                                                            <input value="114" type="checkbox" name="post_category[]" id="in-category-114" /> OpenSrc
                                                        </label>
                                                        <ul class='children'>
                                                            <li id='category-97' class="popular-category">
                                                                <label class="selectit">
                                                                    <input value="97" type="checkbox" name="post_category[]" id="in-category-97" /> Linux
                                                                </label>
                                                            </li>
                                                        </ul>
                                                    </li>
                                                    <li id='category-99' class="popular-category">
                                                        <label class="selectit">
                                                            <input value="99" type="checkbox" name="post_category[]" id="in-category-99" /> Web
                                                        </label>
                                                    </li>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div id="tagsdiv-post_tag" class="postbox " >
                                    <div class="handlediv" title="点击以切换">
                                        <br />
                                    </div>
                                    <h3 class='hndle'><span>标签</span></h3>
                                    <div class="inside">
                                        <div class="tagsdiv" id="post_tag">
                                            <div class="jaxtag">
                                                <div class="nojs-tags hide-if-js">
                                                    <p>添加或删除标签</p>
                                                    <textarea name="tax_input[post_tag]" rows="3" cols="20" class="the-tags" id="tax-input-post_tag" ></textarea>
                                                </div>
                                                <div class="ajaxtag hide-if-no-js">
                                                    <label class="screen-reader-text" for="new-tag-post_tag">标签</label>
                                                    <div class="taghint">添加新标签</div>
                                                    <p><input type="text" id="new-tag-post_tag" name="newtag[post_tag]" class="newtag form-input-tip" size="16" autocomplete="off" value="" />
                                                        <input type="button" class="button tagadd" value="添加" tabindex="3" />
                                                    </p>
                                                </div>
                                                <p class="howto">多个标签请用英文逗号（,）分开</p>
                                            </div>
                                            <div class="tagchecklist"></div>
                                            
                                        </div>
                                        <p class="hide-if-no-js"><a href="#titlediv" class="tagcloud-link" id="link-post_tag">从常用标签中选择</a></p>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div id="titlediv">
                            <div id="titlewrap">
                                <label class="hide-if-no-js" style="visibility:hidden" id="title-prompt-text" for="title">在此键入标题</label>
                                <input type="text" name="post_title" size="30" tabindex="1" value="" id="title" autocomplete="off" />
                            </div>
                            <input type="hidden" id="samplepermalinknonce" name="samplepermalinknonce" value="104424e36a" /></div>
                        <div id="postdivrich" class="postarea">
                            <div id="wp-content-wrap" class="wp-editor-wrap tmce-active"><link rel='stylesheet' id='editor-buttons-css'  href='http://www.cherrot.com/wp-includes/css/editor-buttons.css?ver=20111114' type='text/css' media='all' />
                                <div id="wp-content-editor-tools" class="wp-editor-tools"><a id="content-html" class="hide-if-no-js wp-switch-editor switch-html" onclick="switchEditors.switchto(this);">HTML</a>
                                    <a id="content-tmce" class="hide-if-no-js wp-switch-editor switch-tmce" onclick="switchEditors.switchto(this);">可视化</a>
                                </div>
                                <div id="wp-content-editor-container" class="wp-editor-container"><textarea class="wp-editor-area" rows="25" tabindex="1" cols="40" name="content" id="content"></textarea></div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>
