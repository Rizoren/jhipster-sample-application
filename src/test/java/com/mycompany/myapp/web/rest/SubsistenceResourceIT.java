package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterSampleApplicationApp;
import com.mycompany.myapp.domain.Subsistence;
import com.mycompany.myapp.repository.SubsistenceRepository;
import com.mycompany.myapp.repository.search.SubsistenceSearchRepository;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link SubsistenceResource} REST controller.
 */
@SpringBootTest(classes = JhipsterSampleApplicationApp.class)
public class SubsistenceResourceIT {

    private static final Integer DEFAULT_YEAR_SL = 1;
    private static final Integer UPDATED_YEAR_SL = 2;

    private static final Integer DEFAULT_QUARTER_SL = 1;
    private static final Integer UPDATED_QUARTER_SL = 2;

    private static final Instant DEFAULT_DATE_ACCEPT_SL = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_ACCEPT_SL = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_VALUE_PER_CAPITA_SL = 1D;
    private static final Double UPDATED_VALUE_PER_CAPITA_SL = 2D;

    private static final Double DEFAULT_VALUE_FOR_CAPABLE_SL = 1D;
    private static final Double UPDATED_VALUE_FOR_CAPABLE_SL = 2D;

    private static final Double DEFAULT_VALUE_FOR_PENSIONERS_SL = 1D;
    private static final Double UPDATED_VALUE_FOR_PENSIONERS_SL = 2D;

    private static final Double DEFAULT_VALUE_FOR_CHILDREN_SL = 1D;
    private static final Double UPDATED_VALUE_FOR_CHILDREN_SL = 2D;

    @Autowired
    private SubsistenceRepository subsistenceRepository;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.SubsistenceSearchRepositoryMockConfiguration
     */
    @Autowired
    private SubsistenceSearchRepository mockSubsistenceSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restSubsistenceMockMvc;

    private Subsistence subsistence;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SubsistenceResource subsistenceResource = new SubsistenceResource(subsistenceRepository, mockSubsistenceSearchRepository);
        this.restSubsistenceMockMvc = MockMvcBuilders.standaloneSetup(subsistenceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Subsistence createEntity(EntityManager em) {
        Subsistence subsistence = new Subsistence()
            .yearSL(DEFAULT_YEAR_SL)
            .quarterSL(DEFAULT_QUARTER_SL)
            .dateAcceptSL(DEFAULT_DATE_ACCEPT_SL)
            .valuePerCapitaSL(DEFAULT_VALUE_PER_CAPITA_SL)
            .valueForCapableSL(DEFAULT_VALUE_FOR_CAPABLE_SL)
            .valueForPensionersSL(DEFAULT_VALUE_FOR_PENSIONERS_SL)
            .valueForChildrenSL(DEFAULT_VALUE_FOR_CHILDREN_SL);
        return subsistence;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Subsistence createUpdatedEntity(EntityManager em) {
        Subsistence subsistence = new Subsistence()
            .yearSL(UPDATED_YEAR_SL)
            .quarterSL(UPDATED_QUARTER_SL)
            .dateAcceptSL(UPDATED_DATE_ACCEPT_SL)
            .valuePerCapitaSL(UPDATED_VALUE_PER_CAPITA_SL)
            .valueForCapableSL(UPDATED_VALUE_FOR_CAPABLE_SL)
            .valueForPensionersSL(UPDATED_VALUE_FOR_PENSIONERS_SL)
            .valueForChildrenSL(UPDATED_VALUE_FOR_CHILDREN_SL);
        return subsistence;
    }

    @BeforeEach
    public void initTest() {
        subsistence = createEntity(em);
    }

    @Test
    @Transactional
    public void createSubsistence() throws Exception {
        int databaseSizeBeforeCreate = subsistenceRepository.findAll().size();

        // Create the Subsistence
        restSubsistenceMockMvc.perform(post("/api/subsistences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subsistence)))
            .andExpect(status().isCreated());

        // Validate the Subsistence in the database
        List<Subsistence> subsistenceList = subsistenceRepository.findAll();
        assertThat(subsistenceList).hasSize(databaseSizeBeforeCreate + 1);
        Subsistence testSubsistence = subsistenceList.get(subsistenceList.size() - 1);
        assertThat(testSubsistence.getYearSL()).isEqualTo(DEFAULT_YEAR_SL);
        assertThat(testSubsistence.getQuarterSL()).isEqualTo(DEFAULT_QUARTER_SL);
        assertThat(testSubsistence.getDateAcceptSL()).isEqualTo(DEFAULT_DATE_ACCEPT_SL);
        assertThat(testSubsistence.getValuePerCapitaSL()).isEqualTo(DEFAULT_VALUE_PER_CAPITA_SL);
        assertThat(testSubsistence.getValueForCapableSL()).isEqualTo(DEFAULT_VALUE_FOR_CAPABLE_SL);
        assertThat(testSubsistence.getValueForPensionersSL()).isEqualTo(DEFAULT_VALUE_FOR_PENSIONERS_SL);
        assertThat(testSubsistence.getValueForChildrenSL()).isEqualTo(DEFAULT_VALUE_FOR_CHILDREN_SL);

        // Validate the Subsistence in Elasticsearch
        verify(mockSubsistenceSearchRepository, times(1)).save(testSubsistence);
    }

    @Test
    @Transactional
    public void createSubsistenceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = subsistenceRepository.findAll().size();

        // Create the Subsistence with an existing ID
        subsistence.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubsistenceMockMvc.perform(post("/api/subsistences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subsistence)))
            .andExpect(status().isBadRequest());

        // Validate the Subsistence in the database
        List<Subsistence> subsistenceList = subsistenceRepository.findAll();
        assertThat(subsistenceList).hasSize(databaseSizeBeforeCreate);

        // Validate the Subsistence in Elasticsearch
        verify(mockSubsistenceSearchRepository, times(0)).save(subsistence);
    }


    @Test
    @Transactional
    public void getAllSubsistences() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList
        restSubsistenceMockMvc.perform(get("/api/subsistences?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subsistence.getId().intValue())))
            .andExpect(jsonPath("$.[*].yearSL").value(hasItem(DEFAULT_YEAR_SL)))
            .andExpect(jsonPath("$.[*].quarterSL").value(hasItem(DEFAULT_QUARTER_SL)))
            .andExpect(jsonPath("$.[*].dateAcceptSL").value(hasItem(DEFAULT_DATE_ACCEPT_SL.toString())))
            .andExpect(jsonPath("$.[*].valuePerCapitaSL").value(hasItem(DEFAULT_VALUE_PER_CAPITA_SL.doubleValue())))
            .andExpect(jsonPath("$.[*].valueForCapableSL").value(hasItem(DEFAULT_VALUE_FOR_CAPABLE_SL.doubleValue())))
            .andExpect(jsonPath("$.[*].valueForPensionersSL").value(hasItem(DEFAULT_VALUE_FOR_PENSIONERS_SL.doubleValue())))
            .andExpect(jsonPath("$.[*].valueForChildrenSL").value(hasItem(DEFAULT_VALUE_FOR_CHILDREN_SL.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getSubsistence() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get the subsistence
        restSubsistenceMockMvc.perform(get("/api/subsistences/{id}", subsistence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(subsistence.getId().intValue()))
            .andExpect(jsonPath("$.yearSL").value(DEFAULT_YEAR_SL))
            .andExpect(jsonPath("$.quarterSL").value(DEFAULT_QUARTER_SL))
            .andExpect(jsonPath("$.dateAcceptSL").value(DEFAULT_DATE_ACCEPT_SL.toString()))
            .andExpect(jsonPath("$.valuePerCapitaSL").value(DEFAULT_VALUE_PER_CAPITA_SL.doubleValue()))
            .andExpect(jsonPath("$.valueForCapableSL").value(DEFAULT_VALUE_FOR_CAPABLE_SL.doubleValue()))
            .andExpect(jsonPath("$.valueForPensionersSL").value(DEFAULT_VALUE_FOR_PENSIONERS_SL.doubleValue()))
            .andExpect(jsonPath("$.valueForChildrenSL").value(DEFAULT_VALUE_FOR_CHILDREN_SL.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSubsistence() throws Exception {
        // Get the subsistence
        restSubsistenceMockMvc.perform(get("/api/subsistences/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubsistence() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        int databaseSizeBeforeUpdate = subsistenceRepository.findAll().size();

        // Update the subsistence
        Subsistence updatedSubsistence = subsistenceRepository.findById(subsistence.getId()).get();
        // Disconnect from session so that the updates on updatedSubsistence are not directly saved in db
        em.detach(updatedSubsistence);
        updatedSubsistence
            .yearSL(UPDATED_YEAR_SL)
            .quarterSL(UPDATED_QUARTER_SL)
            .dateAcceptSL(UPDATED_DATE_ACCEPT_SL)
            .valuePerCapitaSL(UPDATED_VALUE_PER_CAPITA_SL)
            .valueForCapableSL(UPDATED_VALUE_FOR_CAPABLE_SL)
            .valueForPensionersSL(UPDATED_VALUE_FOR_PENSIONERS_SL)
            .valueForChildrenSL(UPDATED_VALUE_FOR_CHILDREN_SL);

        restSubsistenceMockMvc.perform(put("/api/subsistences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSubsistence)))
            .andExpect(status().isOk());

        // Validate the Subsistence in the database
        List<Subsistence> subsistenceList = subsistenceRepository.findAll();
        assertThat(subsistenceList).hasSize(databaseSizeBeforeUpdate);
        Subsistence testSubsistence = subsistenceList.get(subsistenceList.size() - 1);
        assertThat(testSubsistence.getYearSL()).isEqualTo(UPDATED_YEAR_SL);
        assertThat(testSubsistence.getQuarterSL()).isEqualTo(UPDATED_QUARTER_SL);
        assertThat(testSubsistence.getDateAcceptSL()).isEqualTo(UPDATED_DATE_ACCEPT_SL);
        assertThat(testSubsistence.getValuePerCapitaSL()).isEqualTo(UPDATED_VALUE_PER_CAPITA_SL);
        assertThat(testSubsistence.getValueForCapableSL()).isEqualTo(UPDATED_VALUE_FOR_CAPABLE_SL);
        assertThat(testSubsistence.getValueForPensionersSL()).isEqualTo(UPDATED_VALUE_FOR_PENSIONERS_SL);
        assertThat(testSubsistence.getValueForChildrenSL()).isEqualTo(UPDATED_VALUE_FOR_CHILDREN_SL);

        // Validate the Subsistence in Elasticsearch
        verify(mockSubsistenceSearchRepository, times(1)).save(testSubsistence);
    }

    @Test
    @Transactional
    public void updateNonExistingSubsistence() throws Exception {
        int databaseSizeBeforeUpdate = subsistenceRepository.findAll().size();

        // Create the Subsistence

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubsistenceMockMvc.perform(put("/api/subsistences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subsistence)))
            .andExpect(status().isBadRequest());

        // Validate the Subsistence in the database
        List<Subsistence> subsistenceList = subsistenceRepository.findAll();
        assertThat(subsistenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Subsistence in Elasticsearch
        verify(mockSubsistenceSearchRepository, times(0)).save(subsistence);
    }

    @Test
    @Transactional
    public void deleteSubsistence() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        int databaseSizeBeforeDelete = subsistenceRepository.findAll().size();

        // Delete the subsistence
        restSubsistenceMockMvc.perform(delete("/api/subsistences/{id}", subsistence.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Subsistence> subsistenceList = subsistenceRepository.findAll();
        assertThat(subsistenceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Subsistence in Elasticsearch
        verify(mockSubsistenceSearchRepository, times(1)).deleteById(subsistence.getId());
    }

    @Test
    @Transactional
    public void searchSubsistence() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);
        when(mockSubsistenceSearchRepository.search(queryStringQuery("id:" + subsistence.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(subsistence), PageRequest.of(0, 1), 1));
        // Search the subsistence
        restSubsistenceMockMvc.perform(get("/api/_search/subsistences?query=id:" + subsistence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subsistence.getId().intValue())))
            .andExpect(jsonPath("$.[*].yearSL").value(hasItem(DEFAULT_YEAR_SL)))
            .andExpect(jsonPath("$.[*].quarterSL").value(hasItem(DEFAULT_QUARTER_SL)))
            .andExpect(jsonPath("$.[*].dateAcceptSL").value(hasItem(DEFAULT_DATE_ACCEPT_SL.toString())))
            .andExpect(jsonPath("$.[*].valuePerCapitaSL").value(hasItem(DEFAULT_VALUE_PER_CAPITA_SL.doubleValue())))
            .andExpect(jsonPath("$.[*].valueForCapableSL").value(hasItem(DEFAULT_VALUE_FOR_CAPABLE_SL.doubleValue())))
            .andExpect(jsonPath("$.[*].valueForPensionersSL").value(hasItem(DEFAULT_VALUE_FOR_PENSIONERS_SL.doubleValue())))
            .andExpect(jsonPath("$.[*].valueForChildrenSL").value(hasItem(DEFAULT_VALUE_FOR_CHILDREN_SL.doubleValue())));
    }
}
