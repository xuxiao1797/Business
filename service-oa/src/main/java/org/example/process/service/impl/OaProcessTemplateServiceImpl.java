package org.example.process.service.impl;

import com.atguigu.model.process.ProcessTemplate;
import com.atguigu.model.process.ProcessType;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.process.mapper.OaProcessTemplateMapper;
import org.example.process.service.OaProcessTemplateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.process.service.OaProcessTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 审批模板 服务实现类
 * </p>
 *
 * @author xx
 * @since 2023-12-29
 */
@Service
public class OaProcessTemplateServiceImpl extends ServiceImpl<OaProcessTemplateMapper, ProcessTemplate> implements OaProcessTemplateService {

    @Autowired
    private OaProcessTypeService processTypeService;

    @Override
    public IPage<ProcessTemplate> selectPageProcessTemplate(Page<ProcessTemplate> pageParam) {
        Page<ProcessTemplate> processTemplatePage = baseMapper.selectPage(pageParam, null);
        List<ProcessTemplate> processTemplateList = processTemplatePage.getRecords();
        for(ProcessTemplate processTemplate : processTemplateList) {
            Long processTypeId = processTemplate.getProcessTypeId();
            LambdaQueryWrapper<ProcessType> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ProcessType::getId,processTypeId);
            ProcessType processType = processTypeService.getOne(queryWrapper);
            if(processType == null) {
                continue;
            }
            processTemplate.setProcessTypeName(processType.getName());
        }
        return processTemplatePage;
    }

    @Override
    public void publish(Long id) {
        ProcessTemplate processTemplate = baseMapper.selectById(id);
        processTemplate.setStatus(1);
        baseMapper.updateById(processTemplate);

        //TODO:部署流程定义
    }
}
