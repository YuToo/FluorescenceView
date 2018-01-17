package xyz.yutoo.fluorescenceview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by YuToo on 2017/2/28.
 */

public class FluorescenceView extends FrameLayout {
    private int     mWidth;                 // 控件宽度
    private int     mHeight;                // 控件高度
    private int     mParticleRadius = 15;          // 粒子大小基数
    private int     mParticleRandomRadius = 50;    // 随机范围(基数上范围)
    private int     mParticleLife = 3000;          //生命基数（毫秒）
    private int     mParticleRandomLife = 8000;        //随机范围（基数上范围）
    private Random  mRandom;                //随机对象
    private int     mParticleNum = 20;           //粒子数量
    private List<Particle>  mParticles;     //粒子集合
    private int[]   mParticleColors = {0xFF0d4289, 0xff034aa1,0x887b0808, 0xff176bd1, 0xff1f39ff,0x33d4ed00, 0x66ffffff, 0xff777800, 0xff0e2569};//粒子颜色集合
    private Paint   mPaint;


    public FluorescenceView(Context context) {
        super(context);
        init();
    }

    public FluorescenceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FluorescenceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    // 初始化方法
    private void init(){
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mRandom = new Random();
        mParticles = new LinkedList<>();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        Log.d("FluorescenceView", "mWidth:" +mWidth+ ",mHeight:" + mHeight);
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if(mWidth == 0 || mHeight == 0){
            return;
        }
        for(Particle particle : mParticles){
            PointF point = particle.getPoint();
            if(point == null){
                continue;
            }
            Shader shader =  new RadialGradient(point.x, point.y, particle.getRadius(), particle.getColor(), 0x00000000, Shader.TileMode.CLAMP);
            mPaint.setShader(shader);
            mPaint.setAlpha((int)(particle.getTranslate() * 255));
            canvas.drawCircle(point.x, point.y, particle.getRadius(), mPaint);
        }
        List<Particle> cache = new LinkedList<>();
        for(Particle particle : mParticles){
            if(particle.getLife() <= 0){
                cache.add(particle);
            }
        }
        mParticles.removeAll(cache);
        for(int i = 0 ; i < mParticleNum - mParticles.size() ; i ++){
            mParticles.add(randomParticle());
        }
        invalidate();
    }

    private Particle randomParticle(){
        if(mRandom == null){
            mRandom = new Random();
        }
        Particle particle = new Particle();
        // 随机起始位置
        PointF startP = new PointF(mRandom.nextInt(mWidth), mRandom.nextInt(mHeight));
        //随机结束位置
        PointF endP = new PointF(mRandom.nextInt(mWidth), mRandom.nextInt(mHeight));
        particle.setStartPointF(startP);
        particle.setEndPointF(endP);
        // 随机生命
        particle.setLife(mParticleLife + mRandom.nextInt(mParticleRandomLife));
        // 随机大小
        particle.setRadius(mParticleRadius + mRandom.nextInt(mParticleRandomRadius));
        // 随机颜色
        particle.setColor(mParticleColors[mRandom.nextInt(mParticleColors.length)]);
        return particle;
    }



    /**
     * Created by YuToo on 2017/2/28.
     * 荧光对象
     */
    public static class Particle {

        private PointF startPointF;//荧光开始坐标
        private PointF endPointF;//荧光结束点坐标
        private float radius;// 荧光半径
        private long startTime;//开始时间
        private int life;   //生命
        private int color;//颜色

        public Particle(){
            startTime = System.currentTimeMillis();
        }

        public PointF getStartPointF() {
            return startPointF;
        }

        public void setStartPointF(PointF startPointF) {
            this.startPointF = startPointF;
        }

        public PointF getEndPointF() {
            return endPointF;
        }

        public void setEndPointF(PointF endPointF) {
            this.endPointF = endPointF;
        }

        public float getRadius() {
            return radius;
        }

        public void setRadius(float radius) {
            this.radius = radius;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setLife(int life) {
            this.life = life;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        //获取粒子透明度:先透明再实体再透明（二次函数）
        public float getTranslate(){
            // 根据生命计算透明度
            int life = getLife();
            if(life <= 0){
                return 0;
            }else{
                // y = 4x - 4x的平方
                float x = (life * 1.0f / this.life);
                return 4 * x *(1 - x);
            }
        }

        // 获取当前位置
        public PointF getPoint(){
            int life = getLife();
            if(life <= 0){
                return null;
            }else{
                PointF pointF = new PointF();
                pointF.x = endPointF.x + (endPointF.x - startPointF.x) * (life * 1.0f / this.life);
                pointF.y = endPointF.y + (endPointF.y - startPointF.y) * (life * 1.0f / this.life);
                return pointF;
            }
        }

        //获取剩余生命
        public int getLife(){
            return (int)(startTime + life - System.currentTimeMillis());
        }
    }
}




