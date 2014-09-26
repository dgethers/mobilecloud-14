package org.magnum.mobilecloud.video.repository;

import org.magnum.mobilecloud.video.client.VideoSvcApi;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface VideoInMemoryRepository extends CrudRepository<Video, Long> {

    public Collection<Video> findByName(
            @Param(VideoSvcApi.TITLE_PARAMETER) String title);

    public Collection<Video> findByDurationLessThan(
            @Param(VideoSvcApi.DURATION_PARAMETER) long maxduration);
}
