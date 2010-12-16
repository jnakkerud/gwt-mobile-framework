/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import java.util.Date;

/**
 *
 * @author jonnakkerud
 */
public class FeedPanel extends Composite {
    public final static String RSS_IMAGE = "feedIcon";

    interface FeedPanelImages extends ClientBundle {
        @Source("com/nubotech/client/resources/feed-icon-28x28.png")
        ImageResource feedIcon();
    }

    FlowPanel main;

    String image_url;
    String title;
    Date post_time;
    String content;
    String user;

    String link;

    FeedPanelImages images = GWT.create(FeedPanelImages.class);

    public FeedPanel(Date post_time, String title, String image_url, String content, String link) {
        initWidget(main = new FlowPanel());
        this.post_time = post_time;
        this.image_url = image_url;
        this.title = title;
        this.content = content;
        this.link = link;

        addStyleName("message");
        initGui();
    }

    public FeedPanel(Date post_time, String title, String image_url, String content) {
        this(post_time, title, image_url, content, null);
    }


    private void initGui() {
        // addImage
        final Image image = createImage();
        image.addStyleName("networkAvatar");
        main.add(image);

        // add title/username
        if (title != null) {
            main.add(createTitle());
        }
        
        // add post_time
        Anchor time = createPostTime();
        main.add(time);

        // add content
        main.add(createContent());

        // TODO link
    }

    private Image createImage() {
        if (image_url.equals(RSS_IMAGE)) {
            return new Image(images.feedIcon());
        }
        else {
            return new Image(image_url);
        }
    }

    // <a title="rmjphillip" class="_username networkName _userInfoPopup" href="#">rmjphillip</a>
    private HTML createTitle() {
        HTML title_anchor = new HTML(title);
        title_anchor.addStyleName("networkName");
        return title_anchor;
    }

    // <a target="_blank" href="http://twitter.com/rmjphillip/status/12419831563288576" class="postTime">
    private Anchor createPostTime() {
        Anchor post_time_anchor = new Anchor(format(post_time));
        post_time_anchor.addStyleName("postTime");
        return post_time_anchor;
    }

    private HTML createContent() {
        // Make it a <p>
        HTML html_content = new HTML(content);
        html_content.addStyleName("messageContent");
        return html_content;
    }

    private String format(Date d) {
        return d.toString();
    }
}
