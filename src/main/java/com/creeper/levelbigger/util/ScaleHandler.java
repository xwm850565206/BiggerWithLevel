package com.creeper.levelbigger.util;

public class ScaleHandler
{
    public static float getScaleFromLevel(int level)
    {
        level = level / 2; // linear scaled
        float factor = 1;
        if (level < 0)
            factor = (-1f / level);
        else
            factor = (float) Math.sqrt(1 + level);
        return factor;
    }

    public static float getReachDistanceFromLevel(int level)
    {
        // apply attribute
        return getScaleFromLevel(level) - 1;
    }

    public static float getEyeHeightFromLevel(int level)
    {
        return getScaleFromLevel(level);
    }

    public static float getMovementSpeedFromLevel(int level)
    {
        // apply attribute
        return (getScaleFromLevel(level) - 1) / 2;
    }

    public static float getJumpDistanceFromLevel(int level)
    {
        return getScaleFromLevel(level) - 1;
    }

    public static float getJumpMovementFromLevel(int level)
    {
        return Math.max(1, getScaleFromLevel(level) / 10);
    }

    public static float getAttackDamageFromLevel(int level)
    {
        // apply attribute
        return getScaleFromLevel(level) - 1;
    }

    public static float getMaxHealthFromLevel(int level)
    {
        // apply attribute
        return Math.min(50.0f, getScaleFromLevel(level)) - 1;
    }
}
