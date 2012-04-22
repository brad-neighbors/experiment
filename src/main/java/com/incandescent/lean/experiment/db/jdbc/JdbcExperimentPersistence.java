package com.incandescent.lean.experiment.db.jdbc;

import com.incandescent.lean.experiment.Experiment;
import com.incandescent.lean.experiment.ExperimentName;
import com.incandescent.lean.experiment.MultiOutcomeExperiment;
import com.incandescent.lean.experiment.db.ExperimentPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Implements persisting experiments using the Spring JDBC template.
 * @author Brad Neighbors
 */
@Repository
public class JdbcExperimentPersistence implements ExperimentPersistence {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcExperimentPersistence(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void store(final Experiment experiment) {
        jdbcTemplate.update("insert into experiment(experiment_name) values (?)", new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, experiment.getName().val());
            }
        });
    }

    @Override
    public MultiOutcomeExperiment findMultiOutcomeExperimentBy(ExperimentName name) {
        // @todo: hydrate all the associations
        final List<MultiOutcomeExperiment> experiments = jdbcTemplate.query("select experiment_name from experiment",
          new RowMapper<MultiOutcomeExperiment>() {
              public MultiOutcomeExperiment mapRow(ResultSet rs, int rowNum) throws SQLException {
                  return new MultiOutcomeExperiment(new ExperimentName(rs.getString("experiment_name")));
              }
          });
        return experiments.isEmpty() ? null : experiments.get(0);
    }
}
