package com.github.dataflow.dashboard.router;

import java.util.List;
import java.util.Random;

/**
 * 随机
 *
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/14
 */
public class RandomRouter extends AbstractRouter {
    private Random random = new Random();

    @Override
    protected String doNext(List<String> children, String name) {
        int nextIndex = random.nextInt(children.size());
        return children.get(nextIndex);
    }
}
