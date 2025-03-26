<script setup>
import { Edit, Delete } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ref } from 'vue'
//获取所有文章分类数据
import { articleCategoryListService, articleCategoryAddService, articleCategoryUpdateService, articleCategoryDeleteService} from '@/api/article.js'
const categorys = ref([
    {
        "id": 0,
        "categoryName": "",
        "categoryAlias": "",
        "createTime": "",
        "updateTime": ""
    }
])
//弹窗标题
const title = ref('')
//控制添加分类弹窗
const dialogVisible = ref(false)
// 存储表单实例
const categoryFormRef = ref(null);
//添加分类数据模型
const categoryModel = ref({
    categoryName: '',
    categoryAlias: ''
})
const getAllCategory = async () => {
    let result = await articleCategoryListService();
    categorys.value = result.data;
}
getAllCategory();
// 添加分类表单校验
const rules = {
    categoryName: [
        { required: true, message: '请输入分类名称', trigger: 'blur' },
        { validator: validateNotEmpty, trigger: 'blur' }
    ],
    categoryAlias: [
        { required: true, message: '请输入分类别名', trigger: 'blur' },
        { validator: validateNotEmpty, trigger: 'blur' }
    ]
}
// 自定义校验方法，确保数据不能为空或仅包含空格
function validateNotEmpty(rule, value, callback) {
    if (!value || value.trim() === '') {
        callback(new Error('输入内容不能为空'));
    } else {
        callback();
    }
}
const addCategory = async () => {
    // 先进行表单校验
    await categoryFormRef.value.validate(async (valid) => {
        if (!valid) {
            ElMessage.error("请填写完整的分类信息");
            return; // 校验失败，阻止提交
        }
        // 调用后端接口提交数据
        let result = await articleCategoryAddService(categoryModel.value);
        ElMessage.success(result.msg ? result.msg : '添加成功');
        // 清空分类数据模型
        categoryModel.value = {};
        // 隐藏弹窗
        dialogVisible.value = false;
        // 重新获取所有分类数据
        getAllCategory();
    });
}
//修改分类回显
const showDialog = (row) => {
    title.value = '修改分类'
    dialogVisible.value = true
    //将row中的数据赋值给categoryModel
    categoryModel.value.categoryName = row.categoryName
    categoryModel.value.categoryAlias = row.categoryAlias
    //修改的时候必须传递分类的id，所以扩展一个id属性
    categoryModel.value.id = row.id
}
const updateCategory = async () => {
    // 先进行表单校验
    await categoryFormRef.value.validate(async (valid) => {
        if (!valid) {
            ElMessage.error("请填写完整的分类信息");
            return; // 校验失败，阻止提交
        }        
        // 调用后端接口提交数据
        let result = await articleCategoryUpdateService(categoryModel.value);
        ElMessage.success(result.msg ? result.msg : '修改成功');
        // 清空分类数据模型
        categoryModel.value = {};
        // 隐藏弹窗
        dialogVisible.value = false;
        // 重新获取所有分类数据
        getAllCategory();
    });
}
//删除分类
const deleteCategory = (row) => {
    ElMessageBox.confirm(
        '你确认删除该分类信息吗？',
        '温馨提示',
        {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
        .then(async () => {
            //用户点击了确认
            let result = await articleCategoryDeleteService(row.id)
            ElMessage.success('删除成功')
            //再次调用getAllCategory，获取所有文章分类
            getAllCategory()
        })
        .catch(() => {
            //用户点击了取消
            ElMessage({
                type: 'info',
                message: '取消删除',
            })
        })
}
</script>
<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>文章分类</span>
                <div class="extra">
                    <el-button type="primary" @click="title = '添加分类'; dialogVisible = true; categoryModel={}">添加分类</el-button>
                </div>
            </div>
        </template>
        <el-table :data="categorys" style="width: 100%">
            <el-table-column label="序号" width="100" type="index"> </el-table-column>
            <el-table-column label="分类名称" prop="categoryName"></el-table-column>
            <el-table-column label="分类别名" prop="categoryAlias"></el-table-column>
            <el-table-column label="操作" width="100">
                <template #default="{ row }">
                    <el-button :icon="Edit" circle plain type="primary" @click="showDialog(row)"></el-button>
                    <el-button :icon="Delete" circle plain type="danger" @click="deleteCategory(row)"></el-button>
                </template>
            </el-table-column>
            <template #empty>
                <el-empty description="没有数据" />
            </template>
        </el-table>

        <!-- 添加与修改分类弹窗 -->
        <el-dialog v-model="dialogVisible" :title="title" width="30%">
            <el-form ref="categoryFormRef" :model="categoryModel" :rules="rules" label-width="100px"
                style="padding-right: 30px">
                <el-form-item label="分类名称" prop="categoryName">
                    <el-input v-model="categoryModel.categoryName" minlength="1" maxlength="10"></el-input>
                </el-form-item>
                <el-form-item label="分类别名" prop="categoryAlias">
                    <el-input v-model="categoryModel.categoryAlias" minlength="1" maxlength="15"></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="dialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="title === '添加分类' ? addCategory() : updateCategory()"> 确认
                    </el-button>
                </span>
            </template>
        </el-dialog>
    </el-card>
</template>

<style lang="scss" scoped>
.page-container {
    min-height: 100%;
    box-sizing: border-box;

    .header {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }
}
</style>