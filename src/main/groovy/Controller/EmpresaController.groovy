package Controller

import DAO.EmpresaDAO
import Model.EmpresaModel
import com.fasterxml.jackson.databind.JsonNode
import jakarta.servlet.ServletException
import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.sql.Connection
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper


import com.google.gson.Gson

@WebServlet("/empresa")
class EmpresaController extends HttpServlet{
    private static final long serialVersionUID = 1L
    private Gson gson = new Gson()

    EmpresaDAO empresaDao = new EmpresaDAO()

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<EmpresaModel> empresas = empresaDao.listarEmpresas()
        String jsonEmpresas = convertToJSON(empresas);
        ApiUtil.sendJsonResponse(resp,jsonEmpresas)
    }

    private static String convertToJSON(List<EmpresaModel> empresas) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(empresas);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonNode json = ApiUtil.readJsonRequestBody(req)
        List<String> dados = ApiUtil.extractValuesFromJson(json)
        EmpresaDAO.inserirEmpresaNoBanco(dados[0],dados[1],dados[2],dados[3], dados[4])
    }

    static boolean cadastrarEmpresa(EmpresaModel empresa, Scanner scanner) {
        Connection con = EmpresaDAO.getInstance().getConnection()

        int empresaId = EmpresaDAO.inserirEmpresaNoBanco(empresa.nome, empresa.email, empresa.cep, empresa.cnpj, empresa.descricao)

    }
    static List<EmpresaModel> listarEmpresas(){
        return EmpresaDAO.listarEmpresas()
    }
}
