package com.start.service.serviceImpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.start.entitle.Floor;
import com.start.entitle.PageBean;
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

    @Override
    public PageBean<Floor> findfloorsbypostidpage(Integer pageNum, Integer pageSize, Integer id) {
        //1.创建pageBean对象
        PageBean<Floor> pb =new PageBean<>();
        //2.开启分页查询
        PageHelper.startPage(pageNum,pageSize);//开启后自动将 数据分页
        //3.掉用mapper
        List<Floor> as=floorMapper.findfloorsbypostid(id);
        Page<Floor> p=(Page<Floor>)as;//强转时将查到的数据分页并 返回指定页到page中；
        //4.把数据填充到pagebean对象中
        pb.setTotal(p.getTotal());
        pb.setItems(p.getResult());

        return pb;
    }
}
