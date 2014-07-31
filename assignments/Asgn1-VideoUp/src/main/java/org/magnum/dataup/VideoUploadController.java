package org.magnum.dataup;

import org.magnum.dataup.model.Video;
import org.magnum.dataup.model.VideoStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * User: outzider
 * Date: 7/29/14
 * Time: 10:53 AM
 */
@Controller
public class VideoUploadController {

    @Autowired private VideoFileManager videoFileManager;
    private Map<Long, Video> videoMap = new ConcurrentHashMap<>();
    private AtomicLong idCounter = new AtomicLong(0);

    @RequestMapping(value=VideoSvcApi.VIDEO_SVC_PATH, method= RequestMethod.GET)
    public @ResponseBody Collection<Video> getVideoList() {
        return videoMap.values();
    }

    @RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH, method = RequestMethod.POST)
    public
    @ResponseBody
    Video addVideo(@RequestBody Video v) {

        long id = idCounter.incrementAndGet();
        v.setId(id);
        v.setDataUrl(getDataUrl(id));
        videoMap.put(id, v);

        return v;
    }

    private String getDataUrl(long videoId) {
        return getUrlBaseForLocalServer() + "/video/" + videoId + "/data";
    }

    private String getUrlBaseForLocalServer() {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return "http://" + request.getServerName()
                + ((request.getServerPort() != 80) ? ":" + request.getServerPort() : "");
    }

    @RequestMapping(value = VideoSvcApi.VIDEO_DATA_PATH, method = RequestMethod.POST)
    public
    @ResponseBody
    VideoStatus setVideoData(@PathVariable(VideoSvcApi.ID_PARAMETER) long id,
                             @RequestParam(VideoSvcApi.DATA_PARAMETER) MultipartFile videoData) throws IOException {
        Video video = videoMap.get(id);
        if (video == null) {
            throw new ResourceNotFoundException();
        }
        //TODO: Add better checking/error handling of empty/null files

        videoFileManager.saveVideoData(video, videoData.getInputStream());
        VideoStatus videoStatus = new VideoStatus(VideoStatus.VideoState.READY);
        return videoStatus;
    }

    @RequestMapping(value = VideoSvcApi.VIDEO_DATA_PATH, method = RequestMethod.GET)
    public
    @ResponseBody
    HttpServletResponse getData(@PathVariable(VideoSvcApi.ID_PARAMETER) long id,
                                HttpServletResponse servletResponse) throws IOException {

        Video video = videoMap.get(id);
        if (video == null || !videoFileManager.hasVideoData(video)) {
            throw new ResourceNotFoundException();
        }
        ServletOutputStream outputStream = servletResponse.getOutputStream();
        videoFileManager.copyVideoData(video, outputStream);
        servletResponse.setContentType(video.getContentType());

        return servletResponse;
    }
}
