package com.peng.leyou.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.andrewoma.dexx.collection.HashMap;
import com.github.andrewoma.dexx.collection.Map;
import com.mysql.fabric.xmlrpc.base.Array;
import com.peng.leyou.enums.ExceptionEnum;
import com.peng.leyou.exception.LeyouException;
import com.peng.leyou.mapper.SpecGroupMapper;
import com.peng.leyou.mapper.SpecParamMapper;
import com.peng.leyou.pojo.SpecGroup;
import com.peng.leyou.pojo.SpecParam;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**   
* 项目名称：leyou-item-service   
* 类名称：SpecificationService   
* 类描述：   规格相关service
* 创建人：彭坤   
* 创建时间：2018年12月16日 下午1:30:05      
* @version     
*/
@Service
public class SpecificationService {
	
	@Autowired
	private SpecGroupMapper specGroupMapper;
	
	@Autowired
	private SpecParamMapper specParamMapper;

	public List<SpecGroup> findSpecGroupByCid(Long cid) {
		Example example=new Example(SpecGroup.class);
		Criteria createCriteria = example.createCriteria();
		createCriteria.andEqualTo("cid", cid);
		List<SpecGroup> selectByExample = specGroupMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(selectByExample)) {
			throw new LeyouException(ExceptionEnum.QUERY_SPEC_GROUP_NULL);
		}
		return selectByExample;
	}

	public List<SpecParam> findSpecParamList(Long gid,Long cid,Boolean searching) {
		SpecParam specParam=new SpecParam();
		specParam.setGroupId(gid);
		specParam.setCid(cid);
		specParam.setSearching(searching);
		List<SpecParam> select = specParamMapper.select(specParam);
		if(CollectionUtils.isEmpty(select)) {
			throw new LeyouException(ExceptionEnum.QUERY_SPEC_PARAM_NULL);
		}
		return select;
	}

	public List<SpecGroup> querySpecGroupByCid(Long cid) {
		List<SpecGroup> specGroups = findSpecGroupByCid(cid);
		
		/*//查询当前分类下的规格参数
		List<SpecParam> specParamList = findSpecParamList(null, cid, null);
		
		System.out.println(specParamList+"!!!!!!!!!!!!");
		
		//填充param到group中
		Map<Long,List<SpecParam>> map = new HashMap<Long,List<SpecParam>>();
		for (SpecParam specParam : specParamList) {
			if (!map.containsKey(specParam.getGroupId())) {//组id在map中不存在
				map.put(specParam.getGroupId(), Arrays.asList(specParam));
			}else {
				List<SpecParam> list = map.get(specParam.getGroupId());
				System.out.println(list);
				list.add(specParam);
			}
		}
		
		System.out.println(map+"-----------------------------");
		
		for (SpecGroup specGroup : specGroups) {
			specGroup.setParams(map.get(specGroup.getId()));
		}
		
		System.out.println(specGroups+"==================================");*/
		
		
		for (SpecGroup specGroup : specGroups) {
			List<SpecParam> findSpecParamList = findSpecParamList(specGroup.getId(), cid, null);
			specGroup.setParams(findSpecParamList);
		}
		System.out.println(specGroups);
		return specGroups;
	}
	
	
	/*public List<SpecGroup> querySpecGroupByCid(Long cid) {
        //查询规格组
        List<SpecGroup> specGroups = findSpecGroupByCid(cid);
        //查询当前分类下的参数
        List<SpecParam> specParams = findSpecParamList(null, cid, null);
        
        
        

        //先把规格参数变成map,map的key是规格组id,map的值是组下的所有参数
        Map<Long,List<SpecParam>> map =new HashMap<>();
        for (SpecParam param : specParams) {
            if(!map.containsKey(param.getGroupId())){
                //这个组id在map中不存在，新增一个list
                map.put(param.getGroupId(),new ArrayList<>());
            }
            map.get(param.getGroupId()).add(param);
        }
        //填充param到group当中去
        for (SpecGroup group : specGroups) {
            group.setParams(map.get(group.getId()));
        }
        return specGroups;
    }*/
	
}
