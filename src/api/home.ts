import client, { type BaseResponse } from "@/api/client"
import { type IHome } from "@/types/home.type"

export const homeList = async () => {
    const {
        data: {isSuccess, data, message}
    } = await client.get<BaseResponse<IHome>>("/home");
    if (!isSuccess) throw new Error(message)
    return data;
}