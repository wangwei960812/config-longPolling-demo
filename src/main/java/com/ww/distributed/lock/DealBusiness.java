package com.ww.distributed.lock;

@FunctionalInterface
public interface DealBusiness {
    Object deal(Object... params);
}
