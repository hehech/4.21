package com.start.service.serviceImpl;

import com.start.entitle.Floor;
import com.start.mapper.FloorMapper;
import com.start.service.FloorService;
import com.start.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class FloorServiceImpl implements FloorService {
    @Autowired
    private FloorMapper floorMapper;

    @Override
    public List<Floor> findfloorsbypostid(Integer id) {
        return floorMapper.findfloorsbypostid(id);
    }

    @Override
    public Floor findfloorbyid(Long floorId) {
        return floorMapper.findfloorbyid(floorId);
    }

    @Override
    public void addfloor(Floor floor) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Long userId = ((Number) map.get("id")).longValue(); // 安全转换

        floor.setUserId(userId);
        System.out.println("设置成功");
        floor.setCreateTime(new Date());
        floor.setLikeCount(0L);
        floor.setIsDeleted(0);
        floorMapper.addfloor(floor);
    }
}
