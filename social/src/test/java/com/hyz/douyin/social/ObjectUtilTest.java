package com.hyz.douyin.social;

import cn.hutool.core.util.ObjUtil;
import com.hyz.douyin.common.model.vo.UserVO;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.Test;


/**
 * @author HYZ
 * @date 2023/8/23 23:26
 */

public class ObjectUtilTest {
    @Test
    public void objIsNullTest() {
        UserVO vo1 = null;
        UserVO vo2 = new UserVO();
        UserVO vo3 = new UserVO();
        vo3.setId(0L);
        vo3.setName("");
        vo3.setFollowCount(0L);
        vo3.setFollowerCount(0L);
        vo3.setIsFollow(false);
        vo3.setAvatar("");
        vo3.setBackgroundImage("");
        vo3.setSignature("");
        vo3.setTotalFavorited(0L);
        vo3.setWorkCount(0L);

        System.out.println(ObjectUtils.allNotNull( vo1,vo2,vo3));
    }
}
