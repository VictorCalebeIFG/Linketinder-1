package Controller

import DAO.VagasDAO
import Model.VagasModel
import DAO.ConexaoDAO
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

@WebServlet("/vaga")
class VagasController extends HttpServlet{
    private static final long serialVersionUID = 1L
    private Gson gson = new Gson()

    VagasDAO vagasDao = new VagasDAO()

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<VagasModel> vagas = vagasDao.listarVagas()
        String jsonVagas = convertToJSON(vagas);
        ApiUtil.sendJsonResponse(resp,jsonVagas)
    }

    private static String convertToJSON(List<VagasModel> vagas) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(vagas);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonNode json = ApiUtil.readJsonRequestBody(req)
        List<String> dados = ApiUtil.extractValuesFromJson(json)
        VagasDAO.inserirVagasNoBanco(dados[0],dados[1], dados[2] as Double, dados[3] as Integer)
    }


    static Double cadastrarVaga(VagasModel vaga, Scanner scanner) {
        Connection con = ConexaoDAO.getInstance().getConnection()
        int vagaId = VagasDAO.inserirVagasNoBanco(vaga)

        return vagaId
    }
    static List<VagasModel> ListarVagas(){
        return VagasDAO.listarVagas()
    }

    static Boolean associarCompetencias(Integer vagaId, Integer idCompetencia){
        return VagasDAO.associarCompetencia(vagaId, idCompetencia)
    }
}
