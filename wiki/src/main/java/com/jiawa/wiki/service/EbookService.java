package com.jiawa.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.wiki.domain.Ebook;
import com.jiawa.wiki.domain.EbookExample;
import com.jiawa.wiki.mapper.EbookMapper;
import com.jiawa.wiki.req.EbookReq;
import com.jiawa.wiki.resp.EbookResp;
import com.jiawa.wiki.resp.PageResp;
import com.jiawa.wiki.util.CopyUtil;
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

    public PageResp<EbookResp> list(EbookReq req){

        //mybatis逆向的类
        EbookExample ebookExample = new EbookExample();
        //  .Criteria 可以看做一个where条件
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
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
        List<EbookResp> list = CopyUtil.copyList(ebookslist, EbookResp.class);

        PageResp<EbookResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageinfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }
}
