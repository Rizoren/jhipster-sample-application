package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterSampleApplicationApp;
import com.mycompany.myapp.domain.Subsistence;
import com.mycompany.myapp.domain.Document;
import com.mycompany.myapp.domain.Region;
import com.mycompany.myapp.repository.SubsistenceRepository;
import com.mycompany.myapp.repository.search.SubsistenceSearchRepository;
import com.mycompany.myapp.service.SubsistenceService;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;
import com.mycompany.myapp.service.dto.SubsistenceCriteria;
import com.mycompany.myapp.service.SubsistenceQueryService;

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

    private static final String DEFAULT_YEAR_SL = "AAAAAAAAAA";
    private static final String UPDATED_YEAR_SL = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUARTER_SL = 1;
    private static final Integer UPDATED_QUARTER_SL = 2;
    private static final Integer SMALLER_QUARTER_SL = 1 - 1;

    private static final Instant DEFAULT_DATE_ACCEPT_SL = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_ACCEPT_SL = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_VALUE_PER_CAPITA_SL = 0D;
    private static final Double UPDATED_VALUE_PER_CAPITA_SL = 1D;
    private static final Double SMALLER_VALUE_PER_CAPITA_SL = 0D - 1D;

    private static final Double DEFAULT_VALUE_FOR_CAPABLE_SL = 0D;
    private static final Double UPDATED_VALUE_FOR_CAPABLE_SL = 1D;
    private static final Double SMALLER_VALUE_FOR_CAPABLE_SL = 0D - 1D;

    private static final Double DEFAULT_VALUE_FOR_PENSIONERS_SL = 0D;
    private static final Double UPDATED_VALUE_FOR_PENSIONERS_SL = 1D;
    private static final Double SMALLER_VALUE_FOR_PENSIONERS_SL = 0D - 1D;

    private static final Double DEFAULT_VALUE_FOR_CHILDREN_SL = 0D;
    private static final Double UPDATED_VALUE_FOR_CHILDREN_SL = 1D;
    private static final Double SMALLER_VALUE_FOR_CHILDREN_SL = 0D - 1D;

    @Autowired
    private SubsistenceRepository subsistenceRepository;

    @Autowired
    private SubsistenceService subsistenceService;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.SubsistenceSearchRepositoryMockConfiguration
     */
    @Autowired
    private SubsistenceSearchRepository mockSubsistenceSearchRepository;

    @Autowired
    private SubsistenceQueryService subsistenceQueryService;

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
        final SubsistenceResource subsistenceResource = new SubsistenceResource(subsistenceService, subsistenceQueryService);
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
    public void checkYearSLIsRequired() throws Exception {
        int databaseSizeBeforeTest = subsistenceRepository.findAll().size();
        // set the field null
        subsistence.setYearSL(null);

        // Create the Subsistence, which fails.

        restSubsistenceMockMvc.perform(post("/api/subsistences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subsistence)))
            .andExpect(status().isBadRequest());

        List<Subsistence> subsistenceList = subsistenceRepository.findAll();
        assertThat(subsistenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuarterSLIsRequired() throws Exception {
        int databaseSizeBeforeTest = subsistenceRepository.findAll().size();
        // set the field null
        subsistence.setQuarterSL(null);

        // Create the Subsistence, which fails.

        restSubsistenceMockMvc.perform(post("/api/subsistences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subsistence)))
            .andExpect(status().isBadRequest());

        List<Subsistence> subsistenceList = subsistenceRepository.findAll();
        assertThat(subsistenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateAcceptSLIsRequired() throws Exception {
        int databaseSizeBeforeTest = subsistenceRepository.findAll().size();
        // set the field null
        subsistence.setDateAcceptSL(null);

        // Create the Subsistence, which fails.

        restSubsistenceMockMvc.perform(post("/api/subsistences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(subsistence)))
            .andExpect(status().isBadRequest());

        List<Subsistence> subsistenceList = subsistenceRepository.findAll();
        assertThat(subsistenceList).hasSize(databaseSizeBeforeTest);
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
    public void getSubsistencesByIdFiltering() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        Long id = subsistence.getId();

        defaultSubsistenceShouldBeFound("id.equals=" + id);
        defaultSubsistenceShouldNotBeFound("id.notEquals=" + id);

        defaultSubsistenceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSubsistenceShouldNotBeFound("id.greaterThan=" + id);

        defaultSubsistenceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSubsistenceShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllSubsistencesByYearSLIsEqualToSomething() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where yearSL equals to DEFAULT_YEAR_SL
        defaultSubsistenceShouldBeFound("yearSL.equals=" + DEFAULT_YEAR_SL);

        // Get all the subsistenceList where yearSL equals to UPDATED_YEAR_SL
        defaultSubsistenceShouldNotBeFound("yearSL.equals=" + UPDATED_YEAR_SL);
    }

    @Test
    @Transactional
    public void getAllSubsistencesByYearSLIsNotEqualToSomething() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where yearSL not equals to DEFAULT_YEAR_SL
        defaultSubsistenceShouldNotBeFound("yearSL.notEquals=" + DEFAULT_YEAR_SL);

        // Get all the subsistenceList where yearSL not equals to UPDATED_YEAR_SL
        defaultSubsistenceShouldBeFound("yearSL.notEquals=" + UPDATED_YEAR_SL);
    }

    @Test
    @Transactional
    public void getAllSubsistencesByYearSLIsInShouldWork() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where yearSL in DEFAULT_YEAR_SL or UPDATED_YEAR_SL
        defaultSubsistenceShouldBeFound("yearSL.in=" + DEFAULT_YEAR_SL + "," + UPDATED_YEAR_SL);

        // Get all the subsistenceList where yearSL equals to UPDATED_YEAR_SL
        defaultSubsistenceShouldNotBeFound("yearSL.in=" + UPDATED_YEAR_SL);
    }

    @Test
    @Transactional
    public void getAllSubsistencesByYearSLIsNullOrNotNull() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where yearSL is not null
        defaultSubsistenceShouldBeFound("yearSL.specified=true");

        // Get all the subsistenceList where yearSL is null
        defaultSubsistenceShouldNotBeFound("yearSL.specified=false");
    }
                @Test
    @Transactional
    public void getAllSubsistencesByYearSLContainsSomething() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where yearSL contains DEFAULT_YEAR_SL
        defaultSubsistenceShouldBeFound("yearSL.contains=" + DEFAULT_YEAR_SL);

        // Get all the subsistenceList where yearSL contains UPDATED_YEAR_SL
        defaultSubsistenceShouldNotBeFound("yearSL.contains=" + UPDATED_YEAR_SL);
    }

    @Test
    @Transactional
    public void getAllSubsistencesByYearSLNotContainsSomething() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where yearSL does not contain DEFAULT_YEAR_SL
        defaultSubsistenceShouldNotBeFound("yearSL.doesNotContain=" + DEFAULT_YEAR_SL);

        // Get all the subsistenceList where yearSL does not contain UPDATED_YEAR_SL
        defaultSubsistenceShouldBeFound("yearSL.doesNotContain=" + UPDATED_YEAR_SL);
    }


    @Test
    @Transactional
    public void getAllSubsistencesByQuarterSLIsEqualToSomething() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where quarterSL equals to DEFAULT_QUARTER_SL
        defaultSubsistenceShouldBeFound("quarterSL.equals=" + DEFAULT_QUARTER_SL);

        // Get all the subsistenceList where quarterSL equals to UPDATED_QUARTER_SL
        defaultSubsistenceShouldNotBeFound("quarterSL.equals=" + UPDATED_QUARTER_SL);
    }

    @Test
    @Transactional
    public void getAllSubsistencesByQuarterSLIsNotEqualToSomething() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where quarterSL not equals to DEFAULT_QUARTER_SL
        defaultSubsistenceShouldNotBeFound("quarterSL.notEquals=" + DEFAULT_QUARTER_SL);

        // Get all the subsistenceList where quarterSL not equals to UPDATED_QUARTER_SL
        defaultSubsistenceShouldBeFound("quarterSL.notEquals=" + UPDATED_QUARTER_SL);
    }

    @Test
    @Transactional
    public void getAllSubsistencesByQuarterSLIsInShouldWork() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where quarterSL in DEFAULT_QUARTER_SL or UPDATED_QUARTER_SL
        defaultSubsistenceShouldBeFound("quarterSL.in=" + DEFAULT_QUARTER_SL + "," + UPDATED_QUARTER_SL);

        // Get all the subsistenceList where quarterSL equals to UPDATED_QUARTER_SL
        defaultSubsistenceShouldNotBeFound("quarterSL.in=" + UPDATED_QUARTER_SL);
    }

    @Test
    @Transactional
    public void getAllSubsistencesByQuarterSLIsNullOrNotNull() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where quarterSL is not null
        defaultSubsistenceShouldBeFound("quarterSL.specified=true");

        // Get all the subsistenceList where quarterSL is null
        defaultSubsistenceShouldNotBeFound("quarterSL.specified=false");
    }

    @Test
    @Transactional
    public void getAllSubsistencesByQuarterSLIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where quarterSL is greater than or equal to DEFAULT_QUARTER_SL
        defaultSubsistenceShouldBeFound("quarterSL.greaterThanOrEqual=" + DEFAULT_QUARTER_SL);

        // Get all the subsistenceList where quarterSL is greater than or equal to (DEFAULT_QUARTER_SL + 1)
        defaultSubsistenceShouldNotBeFound("quarterSL.greaterThanOrEqual=" + (DEFAULT_QUARTER_SL + 1));
    }

    @Test
    @Transactional
    public void getAllSubsistencesByQuarterSLIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where quarterSL is less than or equal to DEFAULT_QUARTER_SL
        defaultSubsistenceShouldBeFound("quarterSL.lessThanOrEqual=" + DEFAULT_QUARTER_SL);

        // Get all the subsistenceList where quarterSL is less than or equal to SMALLER_QUARTER_SL
        defaultSubsistenceShouldNotBeFound("quarterSL.lessThanOrEqual=" + SMALLER_QUARTER_SL);
    }

    @Test
    @Transactional
    public void getAllSubsistencesByQuarterSLIsLessThanSomething() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where quarterSL is less than DEFAULT_QUARTER_SL
        defaultSubsistenceShouldNotBeFound("quarterSL.lessThan=" + DEFAULT_QUARTER_SL);

        // Get all the subsistenceList where quarterSL is less than (DEFAULT_QUARTER_SL + 1)
        defaultSubsistenceShouldBeFound("quarterSL.lessThan=" + (DEFAULT_QUARTER_SL + 1));
    }

    @Test
    @Transactional
    public void getAllSubsistencesByQuarterSLIsGreaterThanSomething() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where quarterSL is greater than DEFAULT_QUARTER_SL
        defaultSubsistenceShouldNotBeFound("quarterSL.greaterThan=" + DEFAULT_QUARTER_SL);

        // Get all the subsistenceList where quarterSL is greater than SMALLER_QUARTER_SL
        defaultSubsistenceShouldBeFound("quarterSL.greaterThan=" + SMALLER_QUARTER_SL);
    }


    @Test
    @Transactional
    public void getAllSubsistencesByDateAcceptSLIsEqualToSomething() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where dateAcceptSL equals to DEFAULT_DATE_ACCEPT_SL
        defaultSubsistenceShouldBeFound("dateAcceptSL.equals=" + DEFAULT_DATE_ACCEPT_SL);

        // Get all the subsistenceList where dateAcceptSL equals to UPDATED_DATE_ACCEPT_SL
        defaultSubsistenceShouldNotBeFound("dateAcceptSL.equals=" + UPDATED_DATE_ACCEPT_SL);
    }

    @Test
    @Transactional
    public void getAllSubsistencesByDateAcceptSLIsNotEqualToSomething() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where dateAcceptSL not equals to DEFAULT_DATE_ACCEPT_SL
        defaultSubsistenceShouldNotBeFound("dateAcceptSL.notEquals=" + DEFAULT_DATE_ACCEPT_SL);

        // Get all the subsistenceList where dateAcceptSL not equals to UPDATED_DATE_ACCEPT_SL
        defaultSubsistenceShouldBeFound("dateAcceptSL.notEquals=" + UPDATED_DATE_ACCEPT_SL);
    }

    @Test
    @Transactional
    public void getAllSubsistencesByDateAcceptSLIsInShouldWork() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where dateAcceptSL in DEFAULT_DATE_ACCEPT_SL or UPDATED_DATE_ACCEPT_SL
        defaultSubsistenceShouldBeFound("dateAcceptSL.in=" + DEFAULT_DATE_ACCEPT_SL + "," + UPDATED_DATE_ACCEPT_SL);

        // Get all the subsistenceList where dateAcceptSL equals to UPDATED_DATE_ACCEPT_SL
        defaultSubsistenceShouldNotBeFound("dateAcceptSL.in=" + UPDATED_DATE_ACCEPT_SL);
    }

    @Test
    @Transactional
    public void getAllSubsistencesByDateAcceptSLIsNullOrNotNull() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where dateAcceptSL is not null
        defaultSubsistenceShouldBeFound("dateAcceptSL.specified=true");

        // Get all the subsistenceList where dateAcceptSL is null
        defaultSubsistenceShouldNotBeFound("dateAcceptSL.specified=false");
    }

    @Test
    @Transactional
    public void getAllSubsistencesByValuePerCapitaSLIsEqualToSomething() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where valuePerCapitaSL equals to DEFAULT_VALUE_PER_CAPITA_SL
        defaultSubsistenceShouldBeFound("valuePerCapitaSL.equals=" + DEFAULT_VALUE_PER_CAPITA_SL);

        // Get all the subsistenceList where valuePerCapitaSL equals to UPDATED_VALUE_PER_CAPITA_SL
        defaultSubsistenceShouldNotBeFound("valuePerCapitaSL.equals=" + UPDATED_VALUE_PER_CAPITA_SL);
    }

    @Test
    @Transactional
    public void getAllSubsistencesByValuePerCapitaSLIsNotEqualToSomething() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where valuePerCapitaSL not equals to DEFAULT_VALUE_PER_CAPITA_SL
        defaultSubsistenceShouldNotBeFound("valuePerCapitaSL.notEquals=" + DEFAULT_VALUE_PER_CAPITA_SL);

        // Get all the subsistenceList where valuePerCapitaSL not equals to UPDATED_VALUE_PER_CAPITA_SL
        defaultSubsistenceShouldBeFound("valuePerCapitaSL.notEquals=" + UPDATED_VALUE_PER_CAPITA_SL);
    }

    @Test
    @Transactional
    public void getAllSubsistencesByValuePerCapitaSLIsInShouldWork() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where valuePerCapitaSL in DEFAULT_VALUE_PER_CAPITA_SL or UPDATED_VALUE_PER_CAPITA_SL
        defaultSubsistenceShouldBeFound("valuePerCapitaSL.in=" + DEFAULT_VALUE_PER_CAPITA_SL + "," + UPDATED_VALUE_PER_CAPITA_SL);

        // Get all the subsistenceList where valuePerCapitaSL equals to UPDATED_VALUE_PER_CAPITA_SL
        defaultSubsistenceShouldNotBeFound("valuePerCapitaSL.in=" + UPDATED_VALUE_PER_CAPITA_SL);
    }

    @Test
    @Transactional
    public void getAllSubsistencesByValuePerCapitaSLIsNullOrNotNull() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where valuePerCapitaSL is not null
        defaultSubsistenceShouldBeFound("valuePerCapitaSL.specified=true");

        // Get all the subsistenceList where valuePerCapitaSL is null
        defaultSubsistenceShouldNotBeFound("valuePerCapitaSL.specified=false");
    }

    @Test
    @Transactional
    public void getAllSubsistencesByValuePerCapitaSLIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where valuePerCapitaSL is greater than or equal to DEFAULT_VALUE_PER_CAPITA_SL
        defaultSubsistenceShouldBeFound("valuePerCapitaSL.greaterThanOrEqual=" + DEFAULT_VALUE_PER_CAPITA_SL);

        // Get all the subsistenceList where valuePerCapitaSL is greater than or equal to UPDATED_VALUE_PER_CAPITA_SL
        defaultSubsistenceShouldNotBeFound("valuePerCapitaSL.greaterThanOrEqual=" + UPDATED_VALUE_PER_CAPITA_SL);
    }

    @Test
    @Transactional
    public void getAllSubsistencesByValuePerCapitaSLIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where valuePerCapitaSL is less than or equal to DEFAULT_VALUE_PER_CAPITA_SL
        defaultSubsistenceShouldBeFound("valuePerCapitaSL.lessThanOrEqual=" + DEFAULT_VALUE_PER_CAPITA_SL);

        // Get all the subsistenceList where valuePerCapitaSL is less than or equal to SMALLER_VALUE_PER_CAPITA_SL
        defaultSubsistenceShouldNotBeFound("valuePerCapitaSL.lessThanOrEqual=" + SMALLER_VALUE_PER_CAPITA_SL);
    }

    @Test
    @Transactional
    public void getAllSubsistencesByValuePerCapitaSLIsLessThanSomething() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where valuePerCapitaSL is less than DEFAULT_VALUE_PER_CAPITA_SL
        defaultSubsistenceShouldNotBeFound("valuePerCapitaSL.lessThan=" + DEFAULT_VALUE_PER_CAPITA_SL);

        // Get all the subsistenceList where valuePerCapitaSL is less than UPDATED_VALUE_PER_CAPITA_SL
        defaultSubsistenceShouldBeFound("valuePerCapitaSL.lessThan=" + UPDATED_VALUE_PER_CAPITA_SL);
    }

    @Test
    @Transactional
    public void getAllSubsistencesByValuePerCapitaSLIsGreaterThanSomething() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where valuePerCapitaSL is greater than DEFAULT_VALUE_PER_CAPITA_SL
        defaultSubsistenceShouldNotBeFound("valuePerCapitaSL.greaterThan=" + DEFAULT_VALUE_PER_CAPITA_SL);

        // Get all the subsistenceList where valuePerCapitaSL is greater than SMALLER_VALUE_PER_CAPITA_SL
        defaultSubsistenceShouldBeFound("valuePerCapitaSL.greaterThan=" + SMALLER_VALUE_PER_CAPITA_SL);
    }


    @Test
    @Transactional
    public void getAllSubsistencesByValueForCapableSLIsEqualToSomething() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where valueForCapableSL equals to DEFAULT_VALUE_FOR_CAPABLE_SL
        defaultSubsistenceShouldBeFound("valueForCapableSL.equals=" + DEFAULT_VALUE_FOR_CAPABLE_SL);

        // Get all the subsistenceList where valueForCapableSL equals to UPDATED_VALUE_FOR_CAPABLE_SL
        defaultSubsistenceShouldNotBeFound("valueForCapableSL.equals=" + UPDATED_VALUE_FOR_CAPABLE_SL);
    }

    @Test
    @Transactional
    public void getAllSubsistencesByValueForCapableSLIsNotEqualToSomething() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where valueForCapableSL not equals to DEFAULT_VALUE_FOR_CAPABLE_SL
        defaultSubsistenceShouldNotBeFound("valueForCapableSL.notEquals=" + DEFAULT_VALUE_FOR_CAPABLE_SL);

        // Get all the subsistenceList where valueForCapableSL not equals to UPDATED_VALUE_FOR_CAPABLE_SL
        defaultSubsistenceShouldBeFound("valueForCapableSL.notEquals=" + UPDATED_VALUE_FOR_CAPABLE_SL);
    }

    @Test
    @Transactional
    public void getAllSubsistencesByValueForCapableSLIsInShouldWork() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where valueForCapableSL in DEFAULT_VALUE_FOR_CAPABLE_SL or UPDATED_VALUE_FOR_CAPABLE_SL
        defaultSubsistenceShouldBeFound("valueForCapableSL.in=" + DEFAULT_VALUE_FOR_CAPABLE_SL + "," + UPDATED_VALUE_FOR_CAPABLE_SL);

        // Get all the subsistenceList where valueForCapableSL equals to UPDATED_VALUE_FOR_CAPABLE_SL
        defaultSubsistenceShouldNotBeFound("valueForCapableSL.in=" + UPDATED_VALUE_FOR_CAPABLE_SL);
    }

    @Test
    @Transactional
    public void getAllSubsistencesByValueForCapableSLIsNullOrNotNull() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where valueForCapableSL is not null
        defaultSubsistenceShouldBeFound("valueForCapableSL.specified=true");

        // Get all the subsistenceList where valueForCapableSL is null
        defaultSubsistenceShouldNotBeFound("valueForCapableSL.specified=false");
    }

    @Test
    @Transactional
    public void getAllSubsistencesByValueForCapableSLIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where valueForCapableSL is greater than or equal to DEFAULT_VALUE_FOR_CAPABLE_SL
        defaultSubsistenceShouldBeFound("valueForCapableSL.greaterThanOrEqual=" + DEFAULT_VALUE_FOR_CAPABLE_SL);

        // Get all the subsistenceList where valueForCapableSL is greater than or equal to UPDATED_VALUE_FOR_CAPABLE_SL
        defaultSubsistenceShouldNotBeFound("valueForCapableSL.greaterThanOrEqual=" + UPDATED_VALUE_FOR_CAPABLE_SL);
    }

    @Test
    @Transactional
    public void getAllSubsistencesByValueForCapableSLIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where valueForCapableSL is less than or equal to DEFAULT_VALUE_FOR_CAPABLE_SL
        defaultSubsistenceShouldBeFound("valueForCapableSL.lessThanOrEqual=" + DEFAULT_VALUE_FOR_CAPABLE_SL);

        // Get all the subsistenceList where valueForCapableSL is less than or equal to SMALLER_VALUE_FOR_CAPABLE_SL
        defaultSubsistenceShouldNotBeFound("valueForCapableSL.lessThanOrEqual=" + SMALLER_VALUE_FOR_CAPABLE_SL);
    }

    @Test
    @Transactional
    public void getAllSubsistencesByValueForCapableSLIsLessThanSomething() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where valueForCapableSL is less than DEFAULT_VALUE_FOR_CAPABLE_SL
        defaultSubsistenceShouldNotBeFound("valueForCapableSL.lessThan=" + DEFAULT_VALUE_FOR_CAPABLE_SL);

        // Get all the subsistenceList where valueForCapableSL is less than UPDATED_VALUE_FOR_CAPABLE_SL
        defaultSubsistenceShouldBeFound("valueForCapableSL.lessThan=" + UPDATED_VALUE_FOR_CAPABLE_SL);
    }

    @Test
    @Transactional
    public void getAllSubsistencesByValueForCapableSLIsGreaterThanSomething() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where valueForCapableSL is greater than DEFAULT_VALUE_FOR_CAPABLE_SL
        defaultSubsistenceShouldNotBeFound("valueForCapableSL.greaterThan=" + DEFAULT_VALUE_FOR_CAPABLE_SL);

        // Get all the subsistenceList where valueForCapableSL is greater than SMALLER_VALUE_FOR_CAPABLE_SL
        defaultSubsistenceShouldBeFound("valueForCapableSL.greaterThan=" + SMALLER_VALUE_FOR_CAPABLE_SL);
    }


    @Test
    @Transactional
    public void getAllSubsistencesByValueForPensionersSLIsEqualToSomething() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where valueForPensionersSL equals to DEFAULT_VALUE_FOR_PENSIONERS_SL
        defaultSubsistenceShouldBeFound("valueForPensionersSL.equals=" + DEFAULT_VALUE_FOR_PENSIONERS_SL);

        // Get all the subsistenceList where valueForPensionersSL equals to UPDATED_VALUE_FOR_PENSIONERS_SL
        defaultSubsistenceShouldNotBeFound("valueForPensionersSL.equals=" + UPDATED_VALUE_FOR_PENSIONERS_SL);
    }

    @Test
    @Transactional
    public void getAllSubsistencesByValueForPensionersSLIsNotEqualToSomething() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where valueForPensionersSL not equals to DEFAULT_VALUE_FOR_PENSIONERS_SL
        defaultSubsistenceShouldNotBeFound("valueForPensionersSL.notEquals=" + DEFAULT_VALUE_FOR_PENSIONERS_SL);

        // Get all the subsistenceList where valueForPensionersSL not equals to UPDATED_VALUE_FOR_PENSIONERS_SL
        defaultSubsistenceShouldBeFound("valueForPensionersSL.notEquals=" + UPDATED_VALUE_FOR_PENSIONERS_SL);
    }

    @Test
    @Transactional
    public void getAllSubsistencesByValueForPensionersSLIsInShouldWork() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where valueForPensionersSL in DEFAULT_VALUE_FOR_PENSIONERS_SL or UPDATED_VALUE_FOR_PENSIONERS_SL
        defaultSubsistenceShouldBeFound("valueForPensionersSL.in=" + DEFAULT_VALUE_FOR_PENSIONERS_SL + "," + UPDATED_VALUE_FOR_PENSIONERS_SL);

        // Get all the subsistenceList where valueForPensionersSL equals to UPDATED_VALUE_FOR_PENSIONERS_SL
        defaultSubsistenceShouldNotBeFound("valueForPensionersSL.in=" + UPDATED_VALUE_FOR_PENSIONERS_SL);
    }

    @Test
    @Transactional
    public void getAllSubsistencesByValueForPensionersSLIsNullOrNotNull() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where valueForPensionersSL is not null
        defaultSubsistenceShouldBeFound("valueForPensionersSL.specified=true");

        // Get all the subsistenceList where valueForPensionersSL is null
        defaultSubsistenceShouldNotBeFound("valueForPensionersSL.specified=false");
    }

    @Test
    @Transactional
    public void getAllSubsistencesByValueForPensionersSLIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where valueForPensionersSL is greater than or equal to DEFAULT_VALUE_FOR_PENSIONERS_SL
        defaultSubsistenceShouldBeFound("valueForPensionersSL.greaterThanOrEqual=" + DEFAULT_VALUE_FOR_PENSIONERS_SL);

        // Get all the subsistenceList where valueForPensionersSL is greater than or equal to UPDATED_VALUE_FOR_PENSIONERS_SL
        defaultSubsistenceShouldNotBeFound("valueForPensionersSL.greaterThanOrEqual=" + UPDATED_VALUE_FOR_PENSIONERS_SL);
    }

    @Test
    @Transactional
    public void getAllSubsistencesByValueForPensionersSLIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where valueForPensionersSL is less than or equal to DEFAULT_VALUE_FOR_PENSIONERS_SL
        defaultSubsistenceShouldBeFound("valueForPensionersSL.lessThanOrEqual=" + DEFAULT_VALUE_FOR_PENSIONERS_SL);

        // Get all the subsistenceList where valueForPensionersSL is less than or equal to SMALLER_VALUE_FOR_PENSIONERS_SL
        defaultSubsistenceShouldNotBeFound("valueForPensionersSL.lessThanOrEqual=" + SMALLER_VALUE_FOR_PENSIONERS_SL);
    }

    @Test
    @Transactional
    public void getAllSubsistencesByValueForPensionersSLIsLessThanSomething() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where valueForPensionersSL is less than DEFAULT_VALUE_FOR_PENSIONERS_SL
        defaultSubsistenceShouldNotBeFound("valueForPensionersSL.lessThan=" + DEFAULT_VALUE_FOR_PENSIONERS_SL);

        // Get all the subsistenceList where valueForPensionersSL is less than UPDATED_VALUE_FOR_PENSIONERS_SL
        defaultSubsistenceShouldBeFound("valueForPensionersSL.lessThan=" + UPDATED_VALUE_FOR_PENSIONERS_SL);
    }

    @Test
    @Transactional
    public void getAllSubsistencesByValueForPensionersSLIsGreaterThanSomething() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where valueForPensionersSL is greater than DEFAULT_VALUE_FOR_PENSIONERS_SL
        defaultSubsistenceShouldNotBeFound("valueForPensionersSL.greaterThan=" + DEFAULT_VALUE_FOR_PENSIONERS_SL);

        // Get all the subsistenceList where valueForPensionersSL is greater than SMALLER_VALUE_FOR_PENSIONERS_SL
        defaultSubsistenceShouldBeFound("valueForPensionersSL.greaterThan=" + SMALLER_VALUE_FOR_PENSIONERS_SL);
    }


    @Test
    @Transactional
    public void getAllSubsistencesByValueForChildrenSLIsEqualToSomething() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where valueForChildrenSL equals to DEFAULT_VALUE_FOR_CHILDREN_SL
        defaultSubsistenceShouldBeFound("valueForChildrenSL.equals=" + DEFAULT_VALUE_FOR_CHILDREN_SL);

        // Get all the subsistenceList where valueForChildrenSL equals to UPDATED_VALUE_FOR_CHILDREN_SL
        defaultSubsistenceShouldNotBeFound("valueForChildrenSL.equals=" + UPDATED_VALUE_FOR_CHILDREN_SL);
    }

    @Test
    @Transactional
    public void getAllSubsistencesByValueForChildrenSLIsNotEqualToSomething() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where valueForChildrenSL not equals to DEFAULT_VALUE_FOR_CHILDREN_SL
        defaultSubsistenceShouldNotBeFound("valueForChildrenSL.notEquals=" + DEFAULT_VALUE_FOR_CHILDREN_SL);

        // Get all the subsistenceList where valueForChildrenSL not equals to UPDATED_VALUE_FOR_CHILDREN_SL
        defaultSubsistenceShouldBeFound("valueForChildrenSL.notEquals=" + UPDATED_VALUE_FOR_CHILDREN_SL);
    }

    @Test
    @Transactional
    public void getAllSubsistencesByValueForChildrenSLIsInShouldWork() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where valueForChildrenSL in DEFAULT_VALUE_FOR_CHILDREN_SL or UPDATED_VALUE_FOR_CHILDREN_SL
        defaultSubsistenceShouldBeFound("valueForChildrenSL.in=" + DEFAULT_VALUE_FOR_CHILDREN_SL + "," + UPDATED_VALUE_FOR_CHILDREN_SL);

        // Get all the subsistenceList where valueForChildrenSL equals to UPDATED_VALUE_FOR_CHILDREN_SL
        defaultSubsistenceShouldNotBeFound("valueForChildrenSL.in=" + UPDATED_VALUE_FOR_CHILDREN_SL);
    }

    @Test
    @Transactional
    public void getAllSubsistencesByValueForChildrenSLIsNullOrNotNull() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where valueForChildrenSL is not null
        defaultSubsistenceShouldBeFound("valueForChildrenSL.specified=true");

        // Get all the subsistenceList where valueForChildrenSL is null
        defaultSubsistenceShouldNotBeFound("valueForChildrenSL.specified=false");
    }

    @Test
    @Transactional
    public void getAllSubsistencesByValueForChildrenSLIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where valueForChildrenSL is greater than or equal to DEFAULT_VALUE_FOR_CHILDREN_SL
        defaultSubsistenceShouldBeFound("valueForChildrenSL.greaterThanOrEqual=" + DEFAULT_VALUE_FOR_CHILDREN_SL);

        // Get all the subsistenceList where valueForChildrenSL is greater than or equal to UPDATED_VALUE_FOR_CHILDREN_SL
        defaultSubsistenceShouldNotBeFound("valueForChildrenSL.greaterThanOrEqual=" + UPDATED_VALUE_FOR_CHILDREN_SL);
    }

    @Test
    @Transactional
    public void getAllSubsistencesByValueForChildrenSLIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where valueForChildrenSL is less than or equal to DEFAULT_VALUE_FOR_CHILDREN_SL
        defaultSubsistenceShouldBeFound("valueForChildrenSL.lessThanOrEqual=" + DEFAULT_VALUE_FOR_CHILDREN_SL);

        // Get all the subsistenceList where valueForChildrenSL is less than or equal to SMALLER_VALUE_FOR_CHILDREN_SL
        defaultSubsistenceShouldNotBeFound("valueForChildrenSL.lessThanOrEqual=" + SMALLER_VALUE_FOR_CHILDREN_SL);
    }

    @Test
    @Transactional
    public void getAllSubsistencesByValueForChildrenSLIsLessThanSomething() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where valueForChildrenSL is less than DEFAULT_VALUE_FOR_CHILDREN_SL
        defaultSubsistenceShouldNotBeFound("valueForChildrenSL.lessThan=" + DEFAULT_VALUE_FOR_CHILDREN_SL);

        // Get all the subsistenceList where valueForChildrenSL is less than UPDATED_VALUE_FOR_CHILDREN_SL
        defaultSubsistenceShouldBeFound("valueForChildrenSL.lessThan=" + UPDATED_VALUE_FOR_CHILDREN_SL);
    }

    @Test
    @Transactional
    public void getAllSubsistencesByValueForChildrenSLIsGreaterThanSomething() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);

        // Get all the subsistenceList where valueForChildrenSL is greater than DEFAULT_VALUE_FOR_CHILDREN_SL
        defaultSubsistenceShouldNotBeFound("valueForChildrenSL.greaterThan=" + DEFAULT_VALUE_FOR_CHILDREN_SL);

        // Get all the subsistenceList where valueForChildrenSL is greater than SMALLER_VALUE_FOR_CHILDREN_SL
        defaultSubsistenceShouldBeFound("valueForChildrenSL.greaterThan=" + SMALLER_VALUE_FOR_CHILDREN_SL);
    }


    @Test
    @Transactional
    public void getAllSubsistencesByDocIsEqualToSomething() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);
        Document doc = DocumentResourceIT.createEntity(em);
        em.persist(doc);
        em.flush();
        subsistence.setDoc(doc);
        subsistenceRepository.saveAndFlush(subsistence);
        Long docId = doc.getId();

        // Get all the subsistenceList where doc equals to docId
        defaultSubsistenceShouldBeFound("docId.equals=" + docId);

        // Get all the subsistenceList where doc equals to docId + 1
        defaultSubsistenceShouldNotBeFound("docId.equals=" + (docId + 1));
    }


    @Test
    @Transactional
    public void getAllSubsistencesByRegionIsEqualToSomething() throws Exception {
        // Initialize the database
        subsistenceRepository.saveAndFlush(subsistence);
        Region region = RegionResourceIT.createEntity(em);
        em.persist(region);
        em.flush();
        subsistence.setRegion(region);
        subsistenceRepository.saveAndFlush(subsistence);
        Long regionId = region.getId();

        // Get all the subsistenceList where region equals to regionId
        defaultSubsistenceShouldBeFound("regionId.equals=" + regionId);

        // Get all the subsistenceList where region equals to regionId + 1
        defaultSubsistenceShouldNotBeFound("regionId.equals=" + (regionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSubsistenceShouldBeFound(String filter) throws Exception {
        restSubsistenceMockMvc.perform(get("/api/subsistences?sort=id,desc&" + filter))
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

        // Check, that the count call also returns 1
        restSubsistenceMockMvc.perform(get("/api/subsistences/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSubsistenceShouldNotBeFound(String filter) throws Exception {
        restSubsistenceMockMvc.perform(get("/api/subsistences?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSubsistenceMockMvc.perform(get("/api/subsistences/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
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
        subsistenceService.save(subsistence);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockSubsistenceSearchRepository);

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
        subsistenceService.save(subsistence);

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
        subsistenceService.save(subsistence);
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
