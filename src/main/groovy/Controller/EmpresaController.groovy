package Controller

import DAO.CandidatoDAO
import DAO.EmpresaDAO
import Model.CandidatoModel
import Model.EmpresaModel
import DAO.ConexaoDAO
import com.fasterxml.jackson.databind.JsonNode

import java.sql.Connection
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import javax.servlet.ServletException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import com.google.gson.Gson

@WebServlet("/empresa")
class EmpresaController extends jakarta.servlet.http.HttpServlet{
    private static final long serialVersionUID = 1L
    private Gson gson = new Gson()

    EmpresaDAO empresaDao = new EmpresaDAO()

    @Override
    protected void doGet(jakarta.servlet.http.HttpServletRequest req, jakarta.servlet.http.HttpServletResponse resp) throws jakarta.servlet.ServletException, IOException {
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
    protected void doPost(jakarta.servlet.http.HttpServletRequest req, jakarta.servlet.http.HttpServletResponse resp) throws jakarta.servlet.ServletException, IOException {
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
