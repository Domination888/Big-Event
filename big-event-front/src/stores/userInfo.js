import { defineStore } from "pinia";
import { ref } from "vue";

export const useUserInfoStore = defineStore("userInfo", () => {
    const info = ref({});

    const setInfo = (newInfo) => {
        info.value = newInfo;
    };

    const removeInfo = () => {
        info.value = {};
    };

    return {
        info,
        setInfo,
        removeInfo,
    };
}, {
    persist: true // 持久化存储配置放在 `defineStore` 选项对象中
});
