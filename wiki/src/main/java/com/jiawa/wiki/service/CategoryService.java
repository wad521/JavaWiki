package com.jiawa.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.wiki.domain.Category;
import com.jiawa.wiki.domain.CategoryExample;
import com.jiawa.wiki.mapper.CategoryMapper;
import com.jiawa.wiki.req.CategoryQueryReq;
import com.jiawa.wiki.req.CategorySaveReq;
import com.jiawa.wiki.resp.CategoryQueryResp;
import com.jiawa.wiki.resp.PageResp;
import com.jiawa.wiki.util.CopyUtil;
import com.jiawa.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author:yxl
 **/

@Service
public class CategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private SnowFlake snowFlake;


    /*
     *模糊查询
     * */
    public PageResp<CategoryQueryResp> list(CategoryQueryReq req){

        //mybatis逆向的类
        CategoryExample categoryExample = new CategoryExample();
        //  .Criteria 可以看做一个where条件
        CategoryExample.Criteria criteria = categoryExample.createCriteria();

        //通过CategoryReq继承PageReq实现动态分页
        PageHelper.startPage(req.getPage(),req.getSize());
        List<Category> categoryslist = categoryMapper.selectByExample(categoryExample);

        PageInfo<Category> pageinfo = new PageInfo<>(categoryslist);
        //建议返回前端的数据为total，这样前端可以自己计算页数；
        //防止百万级数据由前端分页，服务器宕机；
        LOG.info("总行数:{}",pageinfo.getTotal());
        LOG.info("总列数:{}",pageinfo.getPages());


        //链表复制
//        List<CategoryResp> respList = new ArrayList<>();
//        for (Category category : categoryslist) {
////            CategoryResp categoryResp = new CategoryResp();
////            //BeanUtils用法   复制对象
////            BeanUtils.copyProperties(category,categoryResp);
//
//            //使用CopyUtil进行单体复制
//            CategoryResp categoryResp = CopyUtil.copy(category, CategoryResp.class);
//            respList.add(categoryResp);
//        }

        //使用CopyUtil进行列表复制
        List<CategoryQueryResp> list = CopyUtil.copyList(categoryslist, CategoryQueryResp.class);

        PageResp<CategoryQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageinfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    /*
     *查询所有
     * */
    public List<CategoryQueryResp> all(CategoryQueryReq req){
        //mybatis逆向的类
        CategoryExample categoryExample = new CategoryExample();
        //  .Criteria 可以看做一个where条件
        CategoryExample.Criteria criteria = categoryExample.createCriteria();

        List<Category> categoryslist = categoryMapper.selectByExample(categoryExample);
        //使用CopyUtil进行列表复制
        List<CategoryQueryResp> list = CopyUtil.copyList(categoryslist, CategoryQueryResp.class);
        LOG.info("返回数据",list);
        return list;
    }

    /*
     *保存
     * */
    public  void save(CategorySaveReq req) {
        //将请求参数变成实体对象
        Category category = CopyUtil.copy(req,Category.class);
//        System.out.println(ObjectUtils.isEmpty(category.getId()));
//        System.out.println(categoryMapper.selectByPrimaryKey(category.getId()));
//        if(ObjectUtils.isEmpty(category.getId()) == false && (categoryMapper.selectByPrimaryKey(category.getId()))==null){
//            //新增
//            categoryMapper.insert(category);
//        }
        //当该书籍的id不存在于数据库中时，新增；
        if(ObjectUtils.isEmpty(category.getId())){
            //新增
            category.setId(snowFlake.nextId());
            categoryMapper.insert(category);
        }else{
            //更新
            categoryMapper.updateByPrimaryKey(category);
        }
    }

    /*
    * 删除
    * */
    public void delete(Long id){
        categoryMapper.deleteByPrimaryKey(id);
    }
}
