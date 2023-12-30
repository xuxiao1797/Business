package org.example.process.service.impl;

import com.atguigu.model.process.ProcessType;
import org.example.process.mapper.OaProcessTypeMapper;
import org.example.process.service.OaProcessTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 审批类型 服务实现类
 * </p>
 *
 * @author xx
 * @since 2023-12-29
 */
@Service
public class OaProcessTypeServiceImpl extends ServiceImpl<OaProcessTypeMapper, ProcessType> implements OaProcessTypeService {

}
