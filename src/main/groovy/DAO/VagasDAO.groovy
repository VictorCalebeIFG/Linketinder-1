package DAO

import Model.VagasModel

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

class VagasDAO {
        private static final String SQL_INSERIR_VAGAS =
                "INSERT INTO vagas (nome, descricao, salario, id_empresa) VALUES (?, ?, ?, ?) RETURNING id"

        private static final String SQL_SELECT_ALL_VAGAS =
                "SELECT v.id, v.nome, v.descricao, v.salario,v.id_empresa, e.nome AS empresa FROM vagas AS v, empresas AS e WHERE v.id_empresa = e.id"

        private static final String SQL_SELECT_COMPETENCIA =
                "SELECT * FROM competencias WHERE id = ?"

        private static final String SQL_INSERT_VAGAS_COMPETENCIA =
                "INSERT INTO vagas_competencias (id_vagas, id_competencias) VALUES (?, ?);"

    static List<VagasModel> listarVagas() {
        Connection con = ConexaoDAO.getInstance().getConnection()

        ResultSet conjuntoResultados = null
        try {
            PreparedStatement stmt = con.prepareStatement(SQL_SELECT_ALL_VAGAS)
            conjuntoResultados = stmt.executeQuery()

            List<VagasModel> listaVagas = []
            while (conjuntoResultados.next()) {
                String nome = conjuntoResultados.getString(2)
                String descricao = conjuntoResultados.getString(3)
                Double salario = conjuntoResultados.getString(4) as Double
                int idEmpresa = conjuntoResultados.getInt(5)
                String nomeEmpresa = conjuntoResultados.getString(6)
                ArrayList<String> competenciasVagas = new ArrayList<String>()

                VagasModel vaga = new VagasModel(nome, descricao, salario, idEmpresa, nomeEmpresa, competenciasVagas)
                listaVagas << vaga
            }
            return listaVagas

        } catch (SQLException e) {
            DatabaseUtilDAO.handleSQLException(e)
        }
    }

        static int inserirVagasNoBanco(VagasModel vaga) {

            Connection con = ConexaoDAO.getInstance().getConnection()
            try {
                PreparedStatement stmtVaga = con.prepareStatement(SQL_INSERIR_VAGAS, Statement.RETURN_GENERATED_KEYS)
                stmtVaga.setString(1, vaga.nome)
                stmtVaga.setString(2, vaga.descricao)
                stmtVaga.setDouble(3, vaga.salario)
                stmtVaga.setInt(4, vaga.idEmpresa)

                int rowsAffected = stmtVaga.executeUpdate()
                if (rowsAffected > 0) {
                    ResultSet generatedKeys = stmtVaga.getGeneratedKeys()
                    int vagaId = -1

                    if (generatedKeys.next()) {
                        vagaId = generatedKeys.getInt(1)
                    }

                    return vagaId
                }
            } catch (SQLException e) {
                DatabaseUtilDAO.handleSQLException(e)
            }
            return -1
        }

    static int inserirVagasNoBanco(String nome,String desc,Double salario, Integer idEmpresa) {

        Connection con = ConexaoDAO.getInstance().getConnection()
        try {
            PreparedStatement stmtVaga = con.prepareStatement(SQL_INSERIR_VAGAS, Statement.RETURN_GENERATED_KEYS)
            stmtVaga.setString(1, nome)
            stmtVaga.setString(2, desc)
            stmtVaga.setDouble(3, salario)
            stmtVaga.setInt(4, idEmpresa)

            int rowsAffected = stmtVaga.executeUpdate()
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmtVaga.getGeneratedKeys()
                int vagaId = -1

                if (generatedKeys.next()) {
                    vagaId = generatedKeys.getInt(1)
                }

                return vagaId
            }
        } catch (SQLException e) {
            DatabaseUtilDAO.handleSQLException(e)
        }
        return -1
    }

        static void listarTodasCompetencias() {
            try {
                DatabaseUtilDAO.listarTodasCompetencias()
            } finally {

            }
        }

        static boolean associarCompetencia(int vagaId, int idCompetencia) {
            Connection con = ConexaoDAO.getInstance().getConnection()

            try {
                PreparedStatement stmtVerificarCompetencia = con.prepareStatement(SQL_SELECT_COMPETENCIA)
                stmtVerificarCompetencia.setInt(1, idCompetencia)
                ResultSet resultado = stmtVerificarCompetencia.executeQuery()

                if (resultado.next()) {
                    String sqlAssociacao = SQL_INSERT_VAGAS_COMPETENCIA
                    PreparedStatement stmtAssociacao = con.prepareStatement(sqlAssociacao)
                    stmtAssociacao.setInt(1, vagaId) as Integer
                    stmtAssociacao.setInt(2, idCompetencia)
                    stmtAssociacao.executeUpdate()
                    println("Competência cadastrada com sucesso")
                } else {
                    println("Competência não encontrada. Tente novamente.")
                }
            } catch (SQLException e) {
                e.printStackTrace()
            }
        }

}