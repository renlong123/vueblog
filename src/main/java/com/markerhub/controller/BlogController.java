package com.markerhub.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.markerhub.common.lang.Result;
import com.markerhub.entity.Blog;
import com.markerhub.service.BlogService;
import com.markerhub.util.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 关注公众号：MarkerHub
 * @since 2020-09-20
 */
@RestController
public class BlogController {

    @Autowired
    BlogService blogService;

    @GetMapping("/blogs")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage){
        Page page = new Page(currentPage,5);
        IPage<Blog> iPage = blogService.page(page, new QueryWrapper<Blog>().orderByDesc("created"));
        System.out.println(iPage);
        return Result.success(iPage);
    }

    @GetMapping("/blog/{id}")
    public Result detail(@PathVariable("id") Long id){
        Blog blog = blogService.getById(id);
        System.out.println(blog);
        Assert.notNull(blog,"该博客已被删除");
        return Result.success(blog);
    }

    @RequiresAuthentication
    @GetMapping("/blog/edit")
    public Result edit(@Validated @RequestBody Blog blog){
        Blog temp = null;
        if(blog.getId() != null){
            temp = blogService.getById(blog.getId());
            Assert.isTrue(temp.getUserId()== ShiroUtil.getProfile().getId(),"没有权限编辑");
        }else{
            temp = new Blog();
            System.out.println(ShiroUtil.getProfile());
            temp.setUserId(ShiroUtil.getProfile().getId());
            temp.setCreated(LocalDateTime.now());
            temp.setStatus(0);
        }
        BeanUtil.copyProperties(blog,temp,"id","userId","created","status");
        blogService.saveOrUpdate(temp);
        return Result.success(null);
    }

    /*
    * 删除，需要权限
    * */
    @RequiresAuthentication
    @DeleteMapping("/blog/delete")
    public Result delete(@RequestBody Blog blog){
        Assert.isTrue(blog.getUserId() == ShiroUtil.getProfile().getId(),"没有权限编辑");
        boolean b = blogService.removeById(blog.getId());
        if(b){
            return Result.success("删除成功");
        }else{
            return Result.fail("删除失败");
        }
    }
}
