//
// Created by taochen on 2019-12-12.
//

#include <jni.h>
#include <android/log.h>

extern "C" {
#include "ffmpeg.h"
}

# define LOG_TAG "NDK_LOG"
# define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

static jint runCmd(JNIEnv *env, jclass clazz, jint cmdLen, jobjectArray cmd) {
    int argc = (env)->GetArrayLength(cmd);
    char *argv[argc];

    int i;
    for (i = 0; i < argc; i++) {
        jstring js = (jstring) (env)->GetObjectArrayElement(cmd, i);
        argv[i] = (char *) (env)->GetStringUTFChars(js, 0);
    }
    return run(argc, argv);
}

static const char *const classPathName = NATIVE_CLASS;

static JNINativeMethod gMethods[] = {
        {"run", "(I[Ljava/lang/String;)I", (jint *) runCmd},
};

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    LOGE("call JNI_OnLoad");
    JNIEnv *env = NULL;
    jclass clazz;
    //获取JNI环境对象
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        return JNI_FALSE;
    }
    //注册本地方法.Load 目标类
    clazz = env->FindClass(classPathName);
    if (clazz == NULL) {
        LOGE("call JNI_OnLoad clazz==NULL");
        return JNI_FALSE;
    }
    //注册本地native方法
    if (env->RegisterNatives(clazz, gMethods, sizeof(gMethods) / sizeof(gMethods[0])) < 0) {
        LOGE("call JNI_OnLoad RegisterNatives failed");
        return JNI_FALSE;
    }

    /* success -- return valid version number */
    return JNI_VERSION_1_6;
}