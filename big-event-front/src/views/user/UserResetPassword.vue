<script setup>
import { ref } from 'vue'
import { useUserInfoStore } from '@/stores/userInfo.js';
import { userPasswordUpdateService } from '@/api/user.js'
import { ElMessage, ElMessageBox } from 'element-plus';
import { useRouter } from 'vue-router';
const router = useRouter()
const userInfoStore = useUserInfoStore()
const userInfo = ref({ ...userInfoStore.info })
const reData = ref({
    old_pwd: '',
    new_pwd: '',
    re_pwd: ''
})
const checkRePassword = (rule, value, callback) => {
    if (value === '') {
        callback(new Error('请再次输入密码'))
    } else if (value !== reData.value.new_pwd) {
        callback(new Error('两次输入密码不一致!'))
    } else {
        callback()
    }
}
const rules = {
    old_pwd: [
        { required: true, message: '请输入旧密码', trigger: 'blur' },
        { min: 5, max: 16, message: '长度应为5-16位字符', trigger: 'blur' }
    ],
    new_pwd: [
        { required: true, message: '请输入新密码', trigger: 'blur' },
        { min: 5, max: 16, message: '长度应为5-16位字符', trigger: 'blur' }
    ],
    re_pwd: [
        { validator: checkRePassword, trigger: 'blur' }
    ]
}
const checkAndPatchRePassword = async () => {
    if (reData.value.new_pwd != reData.value.re_pwd) {
        ElMessage.error('两次输入密码不一致')
        return false;
    }

    ElMessageBox.confirm(
        '你确认要修改密码吗？',
        '温馨提示',
        {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
        .then(async () => {
            try {
                //用户点击了确认
                let result = await userPasswordUpdateService(reData.value)
                ElMessage.success(result.msg ? result.msg : '修改成功')
                console.log(result);
                //跳转到登录页
                router.push('/login')
                return true;
            } catch (error) {
                ElMessage.error(error.message)
            }
        })
        .catch(() => {
            //用户点击了取消
            ElMessage({
                type: 'info',
                message: '取消修改',
            })
        })
}
</script>
<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>修改密码</span>
            </div>
        </template>
        <el-row>
            <el-col :span="12">
                <el-form :model="reData" :rules="rules" label-width="100px" size="large">
                    <el-form-item label="旧密码" prop="old_pwd">
                        <el-input v-model="reData.old_pwd"></el-input>
                    </el-form-item>
                    <el-form-item label="新密码" prop="new_pwd">
                        <el-input v-model="reData.new_pwd"></el-input>
                    </el-form-item>
                    <el-form-item label="重复新密码" prop="re_pwd">
                        <el-input v-model="reData.re_pwd"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="checkAndPatchRePassword">提交修改</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
    </el-card>
</template>