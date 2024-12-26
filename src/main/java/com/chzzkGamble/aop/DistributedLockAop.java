package com.chzzkGamble.aop;

import com.chzzkGamble.config.DistributedLock;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;

@Aspect
@Order(1)  // before @transactional
@Component
@RequiredArgsConstructor
public class DistributedLockAop {

    private static final String LOCK_PREFIX = "lock:";

    private final RedissonClient redissonClient;

    @Around("@annotation(com.chzzkGamble.config.DistributedLock)")
    public Object lock(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        RLock rLock = redissonClient.getLock(LOCK_PREFIX + distributedLock.key());
        try {
            boolean isLocked = rLock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit());
            if (!isLocked) {
                return false;
            }

            return joinPoint.proceed();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            rLock.unlock();
        }

    }
}
