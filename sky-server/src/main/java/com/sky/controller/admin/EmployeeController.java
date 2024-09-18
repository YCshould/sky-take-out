package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);  //给客户端响应
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("退出登入")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 新增员工
     */
    @PostMapping
    @ApiOperation("新增员工")
    public Result update(@RequestBody EmployeeDTO employeeDTO){
        log.info("登入员工数据:{}",employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }

    /**
    *分页查询
    * */
    @GetMapping("/page")
    @ApiOperation("员工分页查询")
    public Result<PageResult> selectpage(EmployeePageQueryDTO employeePageQueryDTO){  //EmployeePageQueryDTO将前端传的参数给封装起来
        log.info("分页所需数据数据:{}",employeePageQueryDTO);
        PageResult pageResult=employeeService.selectpage(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     *修改员工登入状态
     * */
    @ApiOperation("修改员工状态")
    @PostMapping("/status/{status}")
    public Result statusupdate(@PathVariable Integer status,Long id){  //请求体里面的参数可以不加@PathVariable
        log.info("修改员工状态所需数据数据:{},{}",status,id);
        employeeService.statusupdate(status,id);
        return  Result.success();
    }


    /**
     *根据id查询员工
     * */

    @GetMapping("/{id}")
    @ApiOperation("根据id查询员工")
    public Result selectById(@PathVariable Integer id){
        log.info("索要查询id信息，{}",id);
        Employee employee=employeeService.selectById(id);
        return Result.success(employee);
    }

    /**
     *修改员工具体信息
     * */

    @PutMapping
    @ApiOperation("修改员工具体信息")
    public Result updateimfor(@RequestBody EmployeeDTO employeeDTO){
        log.info("索要查询id信息，{}",employeeDTO);
        employeeService.updateimfor(employeeDTO);
        return Result.success();
    }
}
