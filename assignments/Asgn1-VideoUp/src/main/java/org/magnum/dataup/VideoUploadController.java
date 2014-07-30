package org.magnum.dataup;

import com.google.common.collect.Lists;
import org.magnum.dataup.model.Video;
import org.magnum.dataup.model.VideoStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.mime.TypedFile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * User: outzider
 * Date: 7/29/14
 * Time: 10:53 AM
 */
@Controller
public class VideoUploadController implements VideoSvcApi {

    @Autowired private VideoFileManager videoFileManager;
    private List<Video> videoList = new CopyOnWriteArrayList<Video>();

    @Override
    @RequestMapping(value=VideoSvcApi.VIDEO_SVC_PATH, method= RequestMethod.GET)
    public @ResponseBody Collection<Video> getVideoList() {

        return Lists.newArrayList(videoList);
    }

    @Override
    public Video addVideo(@Body Video v) {
        return null;
    }

    @Override
    public VideoStatus setVideoData(@Path(ID_PARAMETER) long id, @Part(DATA_PARAMETER) TypedFile videoData) {
        return null;
    }

    @Override
    public Response getData(@Path(ID_PARAMETER) long id) {
        return null;
    }
}
