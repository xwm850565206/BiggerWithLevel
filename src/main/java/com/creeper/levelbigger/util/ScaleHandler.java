package com.creeper.levelbigger.util;

public class ScaleHandler
{
    public static float getScaleFromLevel(int level)
    {
        float factor = 1;
        if (level < 0)
            factor = (-1f / level);
        else
            factor = (float) Math.sqrt(1 + level);
        return factor;
    }

    public static float getReachDistanceFromLevel(int level)
    {
        return getScaleFromLevel(level);
    }

    public static float getEyeHeightFromLevel(int level)
    {
        return getScaleFromLevel(level);
    }

    public static float getMovementSpeedFromLevel(int level)
    {
        return getScaleFromLevel(level) / 2;
    }

    public static float getJumpDistanceFromLevel(int level)
    {
        return getScaleFromLevel(level);
    }

    public static float getJumpMovementFromLevel(int level)
    {
        return getScaleFromLevel(level) / 30.0f;
    }

    public static float getAttackDamageFromLevel(int level)
    {
        return getScaleFromLevel(level);
    }

    public static float getMaxHealthFromLevel(int level)
    {
        return Math.min(50.0f, getScaleFromLevel(level)) - 1;
    }
}
