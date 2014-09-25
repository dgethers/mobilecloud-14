package org.magnum.mobilecloud.video;

import com.google.common.collect.Lists;
import org.magnum.mobilecloud.video.client.VideoSvcApi;
import org.magnum.mobilecloud.video.repository.Video;
import org.magnum.mobilecloud.video.repository.VideoInMemoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Controller
public class VideoServiceController {

    @Autowired
    private VideoInMemoryRepository videos;

    @RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH, method = RequestMethod.POST)
    public
    @ResponseBody
    Video addVideo(@RequestBody Video v) {

        return videos.save(v);
    }

    @RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH, method = RequestMethod.GET)
    public
    @ResponseBody
    Collection<Video> getVideoList() {

        return Lists.newArrayList(videos.findAll());
    }

    @RequestMapping(value = VideoSvcApi.VIDEO_TITLE_SEARCH_PATH, method = RequestMethod.GET)
    public
    @ResponseBody
    Collection<Video> findByTitle(@RequestParam(VideoSvcApi.TITLE_PARAMETER) String title) {

        return videos.findByName(title);
    }

    @RequestMapping(value = VideoSvcApi.VIDEO_DURATION_SEARCH_PATH, method = RequestMethod.GET)
    public
    @ResponseBody
    Collection<Video> findByDurationLessThan(@RequestParam(VideoSvcApi.DURATION_PARAMETER) long duration) {

        return videos.findByDurationLessThan(duration);
    }

    @RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH + "/{id}/like", method = RequestMethod.POST)
    public void likeVideo(@PathVariable("id") long id) {

        Video video = videos.findOne(id);
        video.setLikes(video.getLikes() + 1);
        videos.save(video);
    }


}
