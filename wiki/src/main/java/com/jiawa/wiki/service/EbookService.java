package com.jiawa.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.wiki.domain.Ebook;
import com.jiawa.wiki.domain.EbookExample;
import com.jiawa.wiki.mapper.EbookMapper;
import com.jiawa.wiki.req.EbookQueryReq;
import com.jiawa.wiki.req.EbookSaveReq;
import com.jiawa.wiki.resp.EbookQueryResp;
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
public class EbookService {

    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);

    @Resource
    private EbookMapper ebookMapper;

    @Resource
    private SnowFlake snowFlake;


    /*
     *模糊查询
     * */
    public PageResp<EbookQueryResp> list(EbookQueryReq req){

        //mybatis逆向的类
        EbookExample ebookExample = new EbookExample();
        //  .Criteria 可以看做一个where条件
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }

        if (!ObjectUtils.isEmpty(req.getCategoryId2())) {
            criteria.andCategory2IdEqualTo(req.getCategoryId2());
        }
        //通过EbookReq继承PageReq实现动态分页
        PageHelper.startPage(req.getPage(),req.getSize());
        List<Ebook> ebookslist = ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook> pageinfo = new PageInfo<>(ebookslist);
        //建议返回前端的数据为total，这样前端可以自己计算页数；
        //防止百万级数据由前端分页，服务器宕机；
        LOG.info("总行数:{}",pageinfo.getTotal());
        LOG.info("总列数:{}",pageinfo.getPages());


        //链表复制
//        List<EbookResp> respList = new ArrayList<>();
//        for (Ebook ebook : ebookslist) {
////            EbookResp ebookResp = new EbookResp();
////            //BeanUtils用法   复制对象
////            BeanUtils.copyProperties(ebook,ebookResp);
//
//            //使用CopyUtil进行单体复制
//            EbookResp ebookResp = CopyUtil.copy(ebook, EbookResp.class);
//            respList.add(ebookResp);
//        }

        //使用CopyUtil进行列表复制
        List<EbookQueryResp> list = CopyUtil.copyList(ebookslist, EbookQueryResp.class);

        PageResp<EbookQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageinfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    /*
     *查询所有
     * */
    public List<EbookQueryResp> all(){
        //mybatis逆向的类
        EbookExample ebookExample = new EbookExample();
        //  .Criteria 可以看做一个where条件
        EbookExample.Criteria criteria = ebookExample.createCriteria();

        List<Ebook> ebookslist = ebookMapper.selectByExample(ebookExample);
        //使用CopyUtil进行列表复制
        List<EbookQueryResp> list = CopyUtil.copyList(ebookslist, EbookQueryResp.class);
        LOG.info("返回数据",list);
        return list;
    }

    /*
     *保存
     * */
    public  void save(EbookSaveReq req) {
        //将请求参数变成实体对象
        Ebook ebook = CopyUtil.copy(req,Ebook.class);
//        System.out.println(ObjectUtils.isEmpty(ebook.getId()));
//        System.out.println(ebookMapper.selectByPrimaryKey(ebook.getId()));
//        if(ObjectUtils.isEmpty(ebook.getId()) == false && (ebookMapper.selectByPrimaryKey(ebook.getId()))==null){
//            //新增
//            ebookMapper.insert(ebook);
//        }
        //当该书籍的id不存在于数据库中时，新增；
        if(ObjectUtils.isEmpty(ebook.getId())){
            //新增
            ebook.setId(snowFlake.nextId());
            ebookMapper.insert(ebook);
        }else{
            //更新
            ebookMapper.updateByPrimaryKey(ebook);
        }
    }

    /*
    * 删除
    * */
    public void delete(Long id){
        ebookMapper.deleteByPrimaryKey(id);
    }
}
