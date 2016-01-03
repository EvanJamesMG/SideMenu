package com.example.evanjames.sidemenu_one;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private LinearLayout add_parent;
    private LinearLayout menu_parent;

    private long per_duration;
    private boolean label = false;
    private Button btn_menu;
    private Button btn_add;
    private static int menu_order = 1;
    private static int add_order = 2;
    private static long DURATION_add = 200;
    private static int MIDDLE = 2;
    private static final long MAX_DURATION = 200;
    private boolean oneshow = false;
    private boolean twoshow = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        menu_parent = (LinearLayout)findViewById(R.id.btn_group);
        add_parent = (LinearLayout)findViewById(R.id.btn_group_two);

        btn_menu = (Button)findViewById(R.id.btn_anim);
        btn_add = (Button)findViewById(R.id.btn_anim_add);


        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!oneshow && !twoshow) {
                    AnimatorSet visAnimSetone = initAnim_open(menu_parent, add_parent, menu_order);
                    visAnimSetone.start();
                    oneshow = true;
                }else if(oneshow && !twoshow){
                    AnimatorSet visAnimSettwo = initAnim_close(menu_parent, add_parent, menu_order);
                    visAnimSettwo.start();
                    oneshow = false;
                }
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!oneshow && !twoshow) {
                    AnimatorSet visAnimSetone = initAnim_open(menu_parent, add_parent, add_order);
                    visAnimSetone.start();
                    twoshow = true;
                }else if(twoshow && !oneshow){
                    AnimatorSet visAnimSettwo = initAnim_close(menu_parent, add_parent, add_order);
                    visAnimSettwo.start();
                    twoshow = false;
                }
            }
        });
    }

    private AnimatorSet initAnim_open(ViewGroup parent,ViewGroup parent_two,int order) {
        ArrayList<Animator> visAnimList = new ArrayList<Animator>();
        AnimatorSet visAnimSet = new AnimatorSet();


        switch (order){

            case 1:
                per_duration=MAX_DURATION/parent.getChildCount();
                for (int i = 0; i < parent.getChildCount(); i++) {
                    ObjectAnimator itemAnim = createItemVisAnim(parent.getChildAt(i), i);
                    visAnimList.add(itemAnim);
                }
                visAnimSet.playSequentially(visAnimList);
                break;
            case 2:
                for (int i = 0; i < parent_two.getChildCount(); i++) {
                    ObjectAnimator itemAnim = null;
                    if (i == 0) {//第一个动画
                        itemAnim = createFlipYShowAnim(parent_two.getChildAt(i));
                    } else {
                        itemAnim = createFlipXShowAnim(parent_two.getChildAt(i));
                    }
                    visAnimList.add(itemAnim);
                }
                visAnimSet.playSequentially(visAnimList);
                break;
            default:
                break;
        }
        return visAnimSet;

    }
    private AnimatorSet initAnim_close(ViewGroup parent,ViewGroup parent_two,int order) {
        ArrayList<Animator> visAnimList = new ArrayList<Animator>();
        AnimatorSet visAnimSet = new AnimatorSet();

        switch (order){

            case 1:
                per_duration=MAX_DURATION/parent.getChildCount();
                for (int i = 0; i < parent.getChildCount(); i++) {
                    ObjectAnimator itemAnim = createItemInvisAnim(parent.getChildAt(i), i);
                    visAnimList.add(itemAnim);
                }
                visAnimSet.playSequentially(visAnimList);
                break;
            case 2:
                visAnimSet = initHiddenAnim(parent_two);
                break;
            default:
                break;
        }
        return visAnimSet;

    }

    /*
    *下面是第一种动画的方法
    * 也就是展开左侧的menu
    */

    //显示动画
    private ObjectAnimator createItemVisAnim(final View target, int index) {
        target.setPivotX(0);
        target.setPivotY(target.getHeight() / 2);
        ObjectAnimator invisToVis = ObjectAnimator.ofFloat(target, "rotationY", 90f, 0f);
        invisToVis.setDuration(MAX_DURATION-per_duration* index);
        invisToVis.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                target.setVisibility(View.VISIBLE);
            }
        });
        return invisToVis;
    }

    //隐藏动画
    private ObjectAnimator createItemInvisAnim(final View target, int index) {
        target.setPivotX(0);
        target.setPivotY(target.getHeight() / 2);
        ObjectAnimator visToInvis = ObjectAnimator.ofFloat(target, "rotationY", 0f, 90f);
        visToInvis.setDuration(per_duration + per_duration * index);
        return visToInvis;
    }



    /*
    * 下面是第二种动画的方法
    * 也就是展开右侧的menu
    */

    //Y show 打开-第一个
    private ObjectAnimator createFlipYShowAnim(final View target) {
        target.setPivotX(target.getWidth());
        target.setPivotY(target.getHeight() / 2);
        ObjectAnimator invisToVis = ObjectAnimator.ofFloat(target, "rotationY", -90f, 0f);
        invisToVis.setDuration(DURATION_add);
        invisToVis.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                target.setVisibility(View.VISIBLE);
            }
        });
        return invisToVis;
    }

    //x show 打开-下半部(第一个之外的)
    private ObjectAnimator createFlipXShowAnim(final View target) {
        target.setPivotX(target.getWidth() / 2);
        target.setPivotY(0);
        ObjectAnimator invisToVis = ObjectAnimator.ofFloat(target, "rotationX", -90f, 0f);
        invisToVis.setDuration(DURATION_add);
        invisToVis.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                target.setVisibility(View.VISIBLE);
            }
        });
        return invisToVis;
    }

    //隐藏部分的初始化
    private AnimatorSet initHiddenAnim(ViewGroup parent) {
        AnimatorSet beforAnim = new AnimatorSet();
        AnimatorSet middleAnim = new AnimatorSet();
        AnimatorSet afterAnim = new AnimatorSet();
        ArrayList<Animator> beforeAnimList = new ArrayList<Animator>();
        ArrayList<Animator> middleAnimList = new ArrayList<Animator>();
        ArrayList<Animator> afterAnimList = new ArrayList<Animator>();
        for (int i = 0; i < parent.getChildCount(); i++) {
            if (i < MIDDLE) {
                ObjectAnimator itemAnim = createFlipXBeforeHiddenAnim(parent.getChildAt(i), i);
                beforeAnimList.add(itemAnim);
            } else if (i > MIDDLE) {
                ObjectAnimator itemAnim = createFlipXAfterHiddenAnim(parent.getChildAt(i));
                afterAnimList.add(itemAnim);
            } else {
                ObjectAnimator itemAnim = createFlipYHiddenAnim(parent.getChildAt(i));
                middleAnimList.add(itemAnim);
            }
        }
        beforAnim.playSequentially(beforeAnimList);
        middleAnim.playSequentially(middleAnimList);
        Collections.reverse(afterAnimList);
        afterAnim.playSequentially(afterAnimList);
        AnimatorSet visAnimSet = new AnimatorSet();
        visAnimSet.play(beforAnim).with(afterAnim).before(middleAnim);
        return visAnimSet;
    }

    //x hidden before 隐藏-上半部
    private ObjectAnimator createFlipXBeforeHiddenAnim(final View target, int i) {
        target.setPivotX(target.getWidth() / 2);
        target.setPivotY(target.getHeight());
        ObjectAnimator visToInvis = ObjectAnimator.ofFloat(target, "rotationX", 0f, 90f);
        if (i == 0) {
            visToInvis.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    //播放完后恢复位置
                    target.setVisibility(View.INVISIBLE);
                    target.setPivotX(target.getWidth() / 2);
                    target.setPivotY(target.getHeight());
                    ObjectAnimator openAnim = ObjectAnimator.ofFloat(target, "rotationX", 90f, 0f);
                    openAnim.start();
                }
            });
        }
        visToInvis.setDuration(DURATION_add);
        return visToInvis;
    }

    //Y hidden 隐藏-中间(点击部分)
    private ObjectAnimator createFlipYHiddenAnim(final View target) {
        target.setPivotX(target.getWidth());
        target.setPivotY(target.getHeight() / 2);
        ObjectAnimator visToInvis = ObjectAnimator.ofFloat(target, "rotationY", 0f, -90f);
        visToInvis.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                target.setVisibility(View.INVISIBLE);
                target.setPivotX(0);
                target.setPivotY(target.getHeight() / 2);
                ObjectAnimator openAnim = ObjectAnimator.ofFloat(target, "rotationY", -90f, 0f);
                openAnim.start();
            }
        });
        visToInvis.setDuration(DURATION_add);
        return visToInvis;
    }

    //x hidden after 隐藏-下半部
    private ObjectAnimator createFlipXAfterHiddenAnim(View target) {
        target.setPivotX(target.getWidth() / 2);
        target.setPivotY(0);
        ObjectAnimator visToInvis = ObjectAnimator.ofFloat(target, "rotationX", 0f, -90f);
        visToInvis.setDuration(DURATION_add);
        return visToInvis;
    }
}
