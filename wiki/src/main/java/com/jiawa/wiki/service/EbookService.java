package com.jiawa.wiki.service;

import com.jiawa.wiki.domain.Ebook;
import com.jiawa.wiki.domain.EbookExample;
import com.jiawa.wiki.mapper.EbookMapper;
import com.jiawa.wiki.req.EbookReq;
import com.jiawa.wiki.resp.EbookResp;
import com.jiawa.wiki.util.CopyUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author:yxl
 **/

@Service
public class EbookService {

    @Resource
    private EbookMapper ebookMapper;

    public List<EbookResp> list(EbookReq req){
        //mybatis逆向的类
        EbookExample ebookExample = new EbookExample();
        //  .Criteria 可以看做一个where条件
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        List<Ebook> ebookslist = ebookMapper.selectByExample(ebookExample);

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
        return list;
    }
}
